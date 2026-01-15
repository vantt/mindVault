/**
 * mindVault Landing Page - JavaScript
 * Handles animations, interactions, and scroll effects
 */

(function () {
    'use strict';

    // ================================================
    // DOM Elements
    // ================================================
    const nav = document.getElementById('nav');
    const navToggle = document.getElementById('nav-toggle');
    const mobileMenu = document.getElementById('mobile-menu');
    const mobileMenuLinks = document.querySelectorAll('.mobile-menu-link, .mobile-menu-cta');

    // ================================================
    // Navigation Scroll Effect
    // ================================================
    let lastScrollY = 0;
    let ticking = false;

    function updateNav() {
        const scrollY = window.scrollY;

        if (scrollY > 50) {
            nav.classList.add('scrolled');
        } else {
            nav.classList.remove('scrolled');
        }

        lastScrollY = scrollY;
        ticking = false;
    }

    function onScroll() {
        if (!ticking) {
            window.requestAnimationFrame(updateNav);
            ticking = true;
        }
    }

    window.addEventListener('scroll', onScroll, { passive: true });

    // Initial check
    updateNav();

    // ================================================
    // Mobile Menu Toggle
    // ================================================
    function toggleMobileMenu() {
        const isActive = mobileMenu.classList.contains('active');

        if (isActive) {
            closeMobileMenu();
        } else {
            openMobileMenu();
        }
    }

    function openMobileMenu() {
        mobileMenu.classList.add('active');
        navToggle.classList.add('active');
        document.body.style.overflow = 'hidden';
    }

    function closeMobileMenu() {
        mobileMenu.classList.remove('active');
        navToggle.classList.remove('active');
        document.body.style.overflow = '';
    }

    navToggle.addEventListener('click', toggleMobileMenu);

    // Close menu when clicking links
    mobileMenuLinks.forEach(link => {
        link.addEventListener('click', closeMobileMenu);
    });

    // Close menu on escape key
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && mobileMenu.classList.contains('active')) {
            closeMobileMenu();
        }
    });

    // ================================================
    // Smooth Scroll for Anchor Links
    // ================================================
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const href = this.getAttribute('href');

            if (href === '#') return;

            const target = document.querySelector(href);

            if (target) {
                e.preventDefault();

                const navHeight = nav.offsetHeight;
                const targetPosition = target.getBoundingClientRect().top + window.scrollY - navHeight - 20;

                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth'
                });
            }
        });
    });

    // ================================================
    // Intersection Observer for Animations (AOS-like)
    // ================================================
    const animatedElements = document.querySelectorAll('[data-aos]');

    const observerOptions = {
        root: null,
        rootMargin: '0px 0px -50px 0px',
        threshold: 0.1
    };

    const animationObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('aos-animate');
                // Optional: unobserve after animation
                // animationObserver.unobserve(entry.target);
            }
        });
    }, observerOptions);

    animatedElements.forEach(el => {
        animationObserver.observe(el);
    });

    // ================================================
    // Terminal Typing Effect
    // ================================================
    function initTerminalTyping() {
        const terminal = document.querySelector('.hero-terminal');

        if (!terminal) return;

        const lines = terminal.querySelectorAll('.terminal-line');
        let delay = 0;

        lines.forEach((line, index) => {
            line.style.opacity = '0';
            line.style.transform = 'translateX(-10px)';

            setTimeout(() => {
                line.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
                line.style.opacity = '1';
                line.style.transform = 'translateX(0)';
            }, delay);

            delay += 300;
        });
    }

    // Run after page load
    window.addEventListener('load', initTerminalTyping);

    // ================================================
    // Parallax Effect for Background Glows
    // ================================================
    const bgGlows = document.querySelectorAll('.bg-glow');

    function updateParallax() {
        const scrollY = window.scrollY;
        const windowHeight = window.innerHeight;
        const scrollPercent = scrollY / windowHeight;

        bgGlows.forEach((glow, index) => {
            const speed = index === 0 ? 0.1 : 0.15;
            const translateY = scrollY * speed;
            glow.style.transform = `translateY(${translateY}px)`;
        });
    }

    // Throttle parallax updates
    let parallaxTicking = false;

    window.addEventListener('scroll', () => {
        if (!parallaxTicking) {
            window.requestAnimationFrame(() => {
                updateParallax();
                parallaxTicking = false;
            });
            parallaxTicking = true;
        }
    }, { passive: true });

    // ================================================
    // Counter Animation
    // ================================================
    function animateCounter(element, target, duration = 2000) {
        let start = 0;
        const startTime = performance.now();

        function update(currentTime) {
            const elapsed = currentTime - startTime;
            const progress = Math.min(elapsed / duration, 1);

            // Easing function (ease-out)
            const easeOut = 1 - Math.pow(1 - progress, 3);

            const current = Math.floor(easeOut * target);
            element.textContent = current;

            if (progress < 1) {
                requestAnimationFrame(update);
            } else {
                element.textContent = target;
            }
        }

        requestAnimationFrame(update);
    }

    // ================================================
    // Copy to Clipboard Simulation
    // ================================================
    function initCopySimulation() {
        const copiedBadge = document.querySelector('.terminal-copied');

        if (!copiedBadge) return;

        // Simulate copy animation
        setInterval(() => {
            copiedBadge.style.animation = 'none';
            copiedBadge.offsetHeight; // Trigger reflow
            copiedBadge.style.animation = 'fadeIn 0.5s ease';
        }, 5000);
    }

    window.addEventListener('load', initCopySimulation);

    // ================================================
    // Reduced Motion Check
    // ================================================
    const prefersReducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)');

    if (prefersReducedMotion.matches) {
        // Disable animations
        document.querySelectorAll('[data-aos]').forEach(el => {
            el.classList.add('aos-animate');
            el.style.transition = 'none';
        });
    }

    // ================================================
    // Button Ripple Effect
    // ================================================
    document.querySelectorAll('.btn').forEach(button => {
        button.addEventListener('click', function (e) {
            const rect = this.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;

            const ripple = document.createElement('span');
            ripple.style.cssText = `
                position: absolute;
                width: 0;
                height: 0;
                background: rgba(255, 255, 255, 0.3);
                border-radius: 50%;
                transform: translate(-50%, -50%);
                pointer-events: none;
                animation: buttonRipple 0.6s ease-out forwards;
                left: ${x}px;
                top: ${y}px;
            `;

            this.style.position = 'relative';
            this.style.overflow = 'hidden';
            this.appendChild(ripple);

            setTimeout(() => {
                ripple.remove();
            }, 600);
        });
    });

    // Add ripple keyframes
    const style = document.createElement('style');
    style.textContent = `
        @keyframes buttonRipple {
            to {
                width: 300px;
                height: 300px;
                opacity: 0;
            }
        }
    `;
    document.head.appendChild(style);

    // ================================================
    // Keyboard Navigation Enhancement
    // ================================================
    document.querySelectorAll('.btn, .nav-link, .footer-link').forEach(el => {
        el.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                this.click();
            }
        });
    });

    // ================================================
    // Focus Visible Polyfill (for older browsers)
    // ================================================
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Tab') {
            document.body.classList.add('keyboard-navigation');
        }
    });

    document.addEventListener('mousedown', function () {
        document.body.classList.remove('keyboard-navigation');
    });

    // Add focus styles
    const focusStyle = document.createElement('style');
    focusStyle.textContent = `
        body:not(.keyboard-navigation) *:focus {
            outline: none;
        }
        body.keyboard-navigation *:focus {
            outline: 2px solid var(--color-primary);
            outline-offset: 2px;
        }
    `;
    document.head.appendChild(focusStyle);

    // ================================================
    // Lazy Load Images (if any)
    // ================================================
    if ('loading' in HTMLImageElement.prototype) {
        // Native lazy loading supported
        document.querySelectorAll('img[loading="lazy"]').forEach(img => {
            img.src = img.dataset.src;
        });
    } else {
        // Fallback for older browsers
        const lazyImages = document.querySelectorAll('img[loading="lazy"]');

        const imageObserver = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    imageObserver.unobserve(img);
                }
            });
        });

        lazyImages.forEach(img => {
            imageObserver.observe(img);
        });
    }

    // ================================================
    // Interactive Recipe Demo
    // ================================================
    function initRecipeDemo() {
        const demoButtons = document.querySelectorAll('.demo-btn');
        const formulaEl = document.getElementById('demo-formula');
        const secretEl = document.getElementById('demo-secret');
        const resultEl = document.getElementById('demo-result');
        const copyBtn = document.getElementById('demo-copy');

        if (!demoButtons.length || !formulaEl) return;

        // Set first button as active
        demoButtons[0]?.classList.add('active');

        demoButtons.forEach(btn => {
            btn.addEventListener('click', function () {
                // Update active state
                demoButtons.forEach(b => b.classList.remove('active'));
                this.classList.add('active');

                const formula = this.dataset.formula;
                const secret = this.dataset.secret;

                // Parse the formula to generate result
                const result = generatePassword(formula, secret);

                // Animate the change
                animateTextChange(formulaEl, formula);
                animateTextChange(secretEl, secret);
                animateTextChange(resultEl, result);
            });
        });

        // Copy button functionality
        if (copyBtn) {
            copyBtn.addEventListener('click', function () {
                const password = resultEl.textContent;
                navigator.clipboard.writeText(password).then(() => {
                    const span = this.querySelector('span');
                    const originalText = span.textContent;
                    span.textContent = 'Copied!';
                    this.classList.add('copied');

                    setTimeout(() => {
                        span.textContent = originalText;
                        this.classList.remove('copied');
                    }, 2000);
                });
            });
        }
    }

    function generatePassword(formula, secret) {
        // Simple password generation based on formula syntax
        // Format: <hash><position><secret#> where position is #, $, @, %
        const match = formula.match(/^(\w+)([#$@%])(\d)(?:_v(\d+))?$/);
        if (!match) return secret + formula;

        const [, hash, position] = match;

        switch (position) {
            case '#': // Prefix: Secret + Hash
                return secret + hash;
            case '$': // Suffix: Hash + Secret
                return hash + secret;
            case '@': // Middle: Ha + Secret + sh
                const mid = Math.floor(hash.length / 2);
                return hash.slice(0, mid) + secret + hash.slice(mid);
            case '%': // Interleave
                let result = '';
                const maxLen = Math.max(hash.length, secret.length);
                for (let i = 0; i < maxLen; i++) {
                    if (i < hash.length) result += hash[i];
                    if (i < secret.length) result += secret[i];
                }
                return result;
            default:
                return secret + hash;
        }
    }

    function animateTextChange(el, newText) {
        el.style.opacity = '0';
        el.style.transform = 'translateY(-5px)';

        setTimeout(() => {
            el.textContent = newText;
            el.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
            el.style.opacity = '1';
            el.style.transform = 'translateY(0)';
        }, 150);
    }

    window.addEventListener('load', initRecipeDemo);

    // ================================================
    // FAQ Accordion
    // ================================================
    function initFAQ() {
        const faqItems = document.querySelectorAll('.faq-item');

        faqItems.forEach(item => {
            const question = item.querySelector('.faq-question');

            if (question) {
                question.addEventListener('click', () => {
                    const isActive = item.classList.contains('active');

                    // Close all other items
                    faqItems.forEach(otherItem => {
                        if (otherItem !== item) {
                            otherItem.classList.remove('active');
                        }
                    });

                    // Toggle current item
                    item.classList.toggle('active', !isActive);
                });
            }
        });
    }

    window.addEventListener('load', initFAQ);

    // ================================================
    // Hacker Terminal Typing Animation
    // ================================================
    function initHackerTerminal() {
        const hackerTerminal = document.querySelector('.hacker-terminal');
        if (!hackerTerminal) return;

        const lines = hackerTerminal.querySelectorAll('.terminal-line');
        let delay = 0;

        lines.forEach((line, index) => {
            line.style.opacity = '0';
            line.style.transform = 'translateX(-10px)';

            setTimeout(() => {
                line.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
                line.style.opacity = '1';
                line.style.transform = 'translateX(0)';
            }, delay);

            // Error lines appear faster
            delay += line.classList.contains('terminal-error') ? 200 : 400;
        });
    }

    // Observe when hacker terminal enters viewport
    const hackerObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                initHackerTerminal();
                hackerObserver.unobserve(entry.target);
            }
        });
    }, { threshold: 0.3 });

    const hackerTerminal = document.querySelector('.hacker-terminal');
    if (hackerTerminal) {
        hackerObserver.observe(hackerTerminal);
    }

    // ================================================
    // Console Easter Egg
    // ================================================
    console.log(`
    ╔═══════════════════════════════════════════════════════╗
    ║                                                       ║
    ║   🔐 mindVault - Secure Password Manager              ║
    ║                                                       ║
    ║   Your passwords stay private. Always.                ║
    ║                                                       ║
    ║   Interested in the code? Check us out on GitHub!     ║
    ║                                                       ║
    ╚═══════════════════════════════════════════════════════╝
    `);

    // ================================================
    // Performance Optimization: Debounce resize handler
    // ================================================
    let resizeTimeout;

    window.addEventListener('resize', () => {
        clearTimeout(resizeTimeout);
        resizeTimeout = setTimeout(() => {
            // Handle any resize-dependent updates
            updateNav();
        }, 150);
    });

})();
