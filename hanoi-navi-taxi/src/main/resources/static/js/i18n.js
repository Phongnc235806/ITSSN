/**
 * =============================================
 * i18n.js - Internationalization / 国際化
 * Language switching between Japanese and Vietnamese
 * 日本語・ベトナム語切り替え
 * =============================================
 */

const LANG_KEY = 'navitaxi_lang';

/**
 * Switch language between JA and VI
 * @param {string} lang - 'ja' or 'vi'
 */
function switchLang(lang) {
    localStorage.setItem(LANG_KEY, lang);

    // Update language switcher buttons
    document.querySelectorAll('.lang-btn').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.lang === lang);
    });

    // Update all text elements with data-ja / data-vi attributes
    document.querySelectorAll('[data-ja]:not([data-i18n-skip])').forEach(el => {
        const text = el.getAttribute(`data-${lang}`);
        if (text) {
            el.textContent = text;
        }
    });

    // Update placeholders
    document.querySelectorAll(`[data-placeholder-${lang}]`).forEach(el => {
        el.placeholder = el.getAttribute(`data-placeholder-${lang}`);
    });

    // Update select options
    document.querySelectorAll('select option[data-ja]').forEach(option => {
        const text = option.getAttribute(`data-${lang}`);
        if (text) {
            option.textContent = text;
        }
    });

    // Update page direction (both languages are LTR)
    document.documentElement.lang = lang;

    // Let pages with dynamic content refresh translated values.
    document.dispatchEvent(new CustomEvent('languageChanged', { detail: { lang } }));
}

/**
 * Get current language
 * @returns {string} 'ja' or 'vi'
 */
function getCurrentLang() {
    return localStorage.getItem(LANG_KEY) || 'ja';
}

/**
 * Get translated text
 * @param {string} jaText - Japanese text
 * @param {string} viText - Vietnamese text
 * @returns {string} Text in current language
 */
function t(jaText, viText) {
    return getCurrentLang() === 'ja' ? jaText : viText;
}

// Initialize language on page load
document.addEventListener('DOMContentLoaded', function() {
    const savedLang = getCurrentLang();
    switchLang(savedLang);
});
