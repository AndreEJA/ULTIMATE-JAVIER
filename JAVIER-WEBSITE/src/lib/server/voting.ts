import { votes, sessions, Vote, VotingSession } from './db';

export interface SubmitVotesRequest {
	judge_id: string;
	votes: Record<string, Record<string, number>>;
}

export interface VotesResponse {
	success: boolean;
	message: string;
	vote_count?: number;
	error?: string;
}

export function submitVotes(data: SubmitVotesRequest): VotesResponse {
	try {
		let voteCount = 0;

		// Procesar y almacenar cada voto
		for (const [faculty, categoryVotes] of Object.entries(data.votes)) {
			for (const [categoryId, score] of Object.entries(categoryVotes)) {
				const vote: Vote = {
					id: `${data.judge_id}-${faculty}-${categoryId}-${Date.now()}`,
					judge_id: data.judge_id,
					faculty_name: faculty,
					category_id: categoryId,
					score: Math.min(100, Math.max(0, score)),
					created_at: new Date()
				};

				votes.push(vote);
				voteCount++;
			}
		}

		// Registrar sesión de votación
		const sessionId = `session-${data.judge_id}-${Date.now()}`;
		const session: VotingSession = {
			id: sessionId,
			judge_id: data.judge_id,
			started_at: new Date(),
			completed_at: new Date(),
			total_votes: voteCount
		};

		sessions.set(sessionId, session);

		return {
			success: true,
			message: 'Votos registrados exitosamente',
			vote_count: voteCount
		};
	} catch (error) {
		return {
			success: false,
			message: 'Error al registrar votos',
			error: error instanceof Error ? error.message : 'Error desconocido'
		};
	}
}

export interface VotingStats {
	total_judges: number;
	total_votes: number;
	total_sessions: number;
	average_votes_per_judge: number;
}

export function getVotingStats(): VotingStats {
	const uniqueJudges = new Set(votes.map((v) => v.judge_id)).size;

	return {
		total_judges: uniqueJudges,
		total_votes: votes.length,
		total_sessions: sessions.size,
		average_votes_per_judge: uniqueJudges > 0 ? votes.length / uniqueJudges : 0
	};
}

export function getVotesForFaculty(faculty: string): Vote[] {
	return votes.filter((v) => v.faculty_name === faculty);
}

export function getVotesForCategory(categoryId: string): Vote[] {
	return votes.filter((v) => v.category_id === categoryId);
}
