/**
 * =============================================
 * auth.js - Authentication Logic
 * 認証ロジック - ログイン・登録処理
 * =============================================
 */

const API_BASE = '/api/auth';

/**
 * Handle Login Form Submission
 * ログインフォーム送信処理
 */
async function handleLogin(event) {
    event.preventDefault();
    
    const email = document.getElementById('loginEmail').value.trim();
    const password = document.getElementById('loginPassword').value;
    const rememberMe = document.getElementById('rememberMe')?.checked || false;
    const btn = document.getElementById('loginBtn');
    const lang = getCurrentLang();

    // Clear errors
    hideAlert();
    clearErrors();

    // Client-side validation
    if (!email) {
        showFieldError('emailError', t('メールアドレスを入力してください', 'Vui lòng nhập email'));
        return;
    }

    if (!password) {
        showFieldError('passwordError', t('パスワードを入力してください', 'Vui lòng nhập mật khẩu'));
        return;
    }

    // Show loading state
    btn.classList.add('btn-loading');
    btn.disabled = true;

    try {
        const response = await fetch(`${API_BASE}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password, rememberMe })
        });

        const result = await response.json();

        if (result.success) {
            const data = result.data;
            
            // Save token and user info
            const userInfo = {
                token: data.token,
                role: data.role,
                userId: data.userId,
                fullName: data.fullName,
                email: data.email
            };
            localStorage.setItem('user', JSON.stringify(userInfo));

            // Set JWT cookie for Thymeleaf pages
            document.cookie = `jwt_token=${data.token}; path=/; max-age=${24 * 60 * 60}; SameSite=Lax`;

            showAlert('success', t('ログインしました！リダイレクト中...', 'Đăng nhập thành công! Đang chuyển hướng...'));

            // Redirect based on role
            setTimeout(() => {
                window.location.href = data.redirectUrl;
            }, 800);
        } else {
            showAlert('error', result.message || t('ログインに失敗しました', 'Đăng nhập thất bại'));
        }
    } catch (error) {
        console.error('Login error:', error);
        showAlert('error', t('ネットワークエラーが発生しました', 'Lỗi kết nối mạng'));
    } finally {
        btn.classList.remove('btn-loading');
        btn.disabled = false;
    }
}

/**
 * Handle Register Form Submission
 * 登録フォーム送信処理
 */
async function handleRegister(event) {
    event.preventDefault();
    
    const fullName = document.getElementById('regFullName').value.trim();
    const email = document.getElementById('regEmail').value.trim();
    const phone = document.getElementById('regPhone')?.value.trim() || '';
    const password = document.getElementById('regPassword').value;
    const confirmPassword = document.getElementById('regConfirmPassword').value;
    const role = document.getElementById('regRole').value;
    const japaneseLevel = document.getElementById('regJapaneseLevel')?.value || '';
    const licenseNumber = document.getElementById('regLicense')?.value.trim() || '';
    const btn = document.getElementById('registerBtn');

    // Clear errors
    hideAlert();
    clearErrors();

    // Client-side validation
    let hasError = false;

    if (!fullName || fullName.length < 2) {
        showFieldError('nameError', t('名前は2文字以上で入力してください', 'Tên phải có ít nhất 2 ký tự'));
        hasError = true;
    }

    if (!email || !isValidEmail(email)) {
        showFieldError('emailError', t('有効なメールアドレスを入力してください', 'Vui lòng nhập email hợp lệ'));
        hasError = true;
    }

    if (!password || password.length < 8) {
        showFieldError('passwordError', t('パスワードは8文字以上必要です', 'Mật khẩu phải có ít nhất 8 ký tự'));
        hasError = true;
    } else if (!isStrongPassword(password)) {
        showFieldError('passwordError', t(
            'パスワードには大文字、小文字、数字、特殊文字が必要です',
            'Mật khẩu phải có chữ hoa, chữ thường, số và ký tự đặc biệt'
        ));
        hasError = true;
    }

    if (password !== confirmPassword) {
        showFieldError('confirmError', t('パスワードが一致しません', 'Mật khẩu xác nhận không khớp'));
        hasError = true;
    }

    if (hasError) return;

    // Show loading
    btn.classList.add('btn-loading');
    btn.disabled = true;

    try {
        const body = {
            fullName, email, password, confirmPassword,
            phoneNumber: phone, role
        };

        if (role === 'DRIVER') {
            body.japaneseLevel = japaneseLevel;
            body.licenseNumber = licenseNumber;
        }

        const response = await fetch(`${API_BASE}/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        const result = await response.json();

        if (result.success) {
            const data = result.data;
            
            // Save token
            const userInfo = {
                token: data.token,
                role: data.role,
                userId: data.userId,
                fullName: data.fullName,
                email: data.email
            };
            localStorage.setItem('user', JSON.stringify(userInfo));
            document.cookie = `jwt_token=${data.token}; path=/; max-age=${24 * 60 * 60}; SameSite=Lax`;

            showAlert('success', t('登録が完了しました！', 'Đăng ký thành công!'));

            setTimeout(() => {
                window.location.href = data.redirectUrl;
            }, 800);
        } else {
            if (result.errors) {
                Object.entries(result.errors).forEach(([field, msg]) => {
                    const errorId = getErrorId(field);
                    if (errorId) showFieldError(errorId, msg);
                });
            }
            showAlert('error', result.message || t('登録に失敗しました', 'Đăng ký thất bại'));
        }
    } catch (error) {
        console.error('Register error:', error);
        showAlert('error', t('ネットワークエラーが発生しました', 'Lỗi kết nối mạng'));
    } finally {
        btn.classList.remove('btn-loading');
        btn.disabled = false;
    }
}

/**
 * Toggle driver-specific fields visibility
 */
function toggleDriverFields() {
    const role = document.getElementById('regRole').value;
    const fields = document.getElementById('driverFields');
    if (fields) {
        fields.classList.toggle('show', role === 'DRIVER');
    }
}

/**
 * Password strength checker
 */
function checkPasswordStrength(password) {
    let score = 0;
    if (password.length >= 8) score++;
    if (password.length >= 12) score++;
    if (/[a-z]/.test(password)) score++;
    if (/[A-Z]/.test(password)) score++;
    if (/[0-9]/.test(password)) score++;
    if (/[^A-Za-z0-9]/.test(password)) score++;
    return score;
}

// Password strength indicator (on register page)
document.addEventListener('DOMContentLoaded', function() {
    const passInput = document.getElementById('regPassword');
    if (passInput) {
        passInput.addEventListener('input', function() {
            const strength = checkPasswordStrength(this.value);
            const bar = document.getElementById('strengthBar');
            const text = document.getElementById('strengthText');
            const container = document.getElementById('passwordStrength');
            
            if (!bar || !text || !container) return;

            container.style.display = this.value ? 'block' : 'none';

            const colors = ['#ef4444', '#ef4444', '#f59e0b', '#f59e0b', '#22c55e', '#22c55e'];
            const labels_ja = ['非常に弱い', '弱い', 'まあまあ', '良い', '強い', '非常に強い'];
            const labels_vi = ['Rất yếu', 'Yếu', 'Trung bình', 'Khá', 'Mạnh', 'Rất mạnh'];
            
            const idx = Math.min(strength, 5);
            bar.style.width = ((idx + 1) / 6 * 100) + '%';
            bar.style.background = colors[idx];
            text.textContent = getCurrentLang() === 'ja' ? labels_ja[idx] : labels_vi[idx];
            text.style.color = colors[idx];
        });
    }
});

// --- Utility Functions ---

function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function isStrongPassword(password) {
    return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/.test(password);
}

function showAlert(type, message) {
    const alert = document.getElementById('alertMessage');
    const icon = document.getElementById('alertIcon');
    const text = document.getElementById('alertText');
    if (!alert) return;
    
    alert.className = `alert alert-${type} show`;
    icon.textContent = type === 'success' ? '✅' : type === 'error' ? '❌' : '⚠️';
    text.textContent = message;
}

function hideAlert() {
    const alert = document.getElementById('alertMessage');
    if (alert) alert.classList.remove('show');
}

function showFieldError(elementId, message) {
    const el = document.getElementById(elementId);
    if (el) {
        el.textContent = message;
        el.classList.add('show');
    }
}

function clearErrors() {
    document.querySelectorAll('.form-error').forEach(el => {
        el.textContent = '';
        el.classList.remove('show');
    });
}

function getErrorId(field) {
    const map = {
        'fullName': 'nameError',
        'email': 'emailError',
        'password': 'passwordError',
        'confirmPassword': 'confirmError',
        'phoneNumber': 'phoneError'
    };
    return map[field];
}
