tailwind.config = {
    theme: {
        extend: {
            colors: {
                primary: '#165DFF',
                secondary: '#FF6B35',
                dark: '#1A1A2E',
                light: '#F8F9FA',
                neutral: '#6C757D'
            },
            fontFamily: {
                sans: ['Inter', 'sans-serif'],
                serif: ['"Playfair Display"', 'serif']
            },
            keyframes: {
                fadeIn: {
                    '0%': { opacity: '0' },
                    '100%': { opacity: '1' }
                },
                slideUp: {
                    '0%': { transform: 'translateY(20px)', opacity: '0' },
                    '100%': { transform: 'translateY(0)', opacity: '1' }
                },
                pulse: {
                    '0%, 100%': { opacity: '1' },
                    '50%': { opacity: '0.5' }
                }
            },
            animation: {
                fadeIn: 'fadeIn 0.6s ease-out forwards',
                slideUp: 'slideUp 0.8s ease-out forwards',
                pulse: 'pulse 2s infinite'
            }
        }
    }
}