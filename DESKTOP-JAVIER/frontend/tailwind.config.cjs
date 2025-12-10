/** @type {import('tailwindcss').Config} */
module.exports = {
	content: ['./src/**/*.{html,js,svelte,ts}'],
	theme: {
		extend: {
			fontFamily: {
				display: ['"Space Grotesk"', 'Inter', 'system-ui', 'sans-serif'],
				body: ['"Space Grotesk"', 'Inter', 'system-ui', 'sans-serif']
			},
			backgroundImage: {
				'grid-sheen': 'radial-gradient(circle at 1px 1px, rgba(255,255,255,0.06) 1px, transparent 0)'
			}
		}
	},
	plugins: [require('@tailwindcss/forms'), require('daisyui')],
	daisyui: {
		logs: false,
		themes: [
			{
				uamjury: {
					primary: '#bff12f',
					secondary: '#1dd6b4',
					accent: '#ff9f1c',
					neutral: '#0f172a',
					'base-100': '#0b1220',
					info: '#38bdf8',
					success: '#22c55e',
					warning: '#fbbf24',
					error: '#ef4444'
				}
			}
		]
	}
};
