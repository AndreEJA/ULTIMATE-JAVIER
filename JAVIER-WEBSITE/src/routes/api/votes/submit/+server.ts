import { json } from '@sveltejs/kit';
import { submitVotes } from '$lib/server/voting';
import type { RequestHandler } from './$types';

export const POST: RequestHandler = async ({ request }) => {
	try {
		const body = await request.json();
		const { judge_id, votes } = body;

		if (!judge_id || !votes) {
			return json(
				{ success: false, error: 'judge_id y votes requeridos' },
				{ status: 400 }
			);
		}

		const result = submitVotes({ judge_id, votes });

		return json(result, {
			status: result.success ? 200 : 400
		});
	} catch (error) {
		return json(
			{ success: false, error: 'Error al procesar solicitud' },
			{ status: 500 }
		);
	}
};
