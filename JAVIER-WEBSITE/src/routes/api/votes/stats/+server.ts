import { json } from '@sveltejs/kit';
import { getVotingStats } from '$lib/server/voting';
import type { RequestHandler } from './$types';

export const GET: RequestHandler = async () => {
	try {
		const stats = getVotingStats();
		return json({ success: true, stats });
	} catch (error) {
		return json(
			{ success: false, error: 'Error al obtener estadísticas' },
			{ status: 500 }
		);
	}
};
