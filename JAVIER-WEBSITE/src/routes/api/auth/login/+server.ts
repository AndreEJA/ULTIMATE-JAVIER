import { json } from '@sveltejs/kit';
import { authenticateJudge } from '$lib/server/auth';
import type { RequestHandler } from './$types';

export const POST: RequestHandler = async ({ request }) => {
	try {
		const body = await request.json();
		const { email, password } = body;

		if (!email || !password) {
			return json(
				{ success: false, error: 'Email y contraseña requeridos' },
				{ status: 400 }
			);
		}

		const result = authenticateJudge({ email, password });

		if (!result.success) {
			return json(
				{ success: false, error: result.error },
				{ status: 401 }
			);
		}

		return json({ success: true, judge: result.judge });
	} catch (error) {
		return json(
			{ success: false, error: 'Error al procesar solicitud' },
			{ status: 500 }
		);
	}
};
