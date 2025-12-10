export interface LoginRequest {
	email: string;
	password: string;
}

export interface LoginResponse {
	success: boolean;
	judge?: {
		id: string;
		name: string;
		email: string;
	};
	error?: string;
}

export interface SubmitVotesRequest {
	judge_id: string;
	votes: Record<string, Record<string, number>>;
}

export interface SubmitVotesResponse {
	success: boolean;
	message: string;
	vote_count?: number;
	error?: string;
}

export async function loginJudge(credentials: LoginRequest): Promise<LoginResponse> {
	try {
		const response = await fetch('/api/auth/login', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(credentials)
		});

		return await response.json();
	} catch (error) {
		return {
			success: false,
			error: 'Error al conectar con el servidor'
		};
	}
}

export async function submitJudgeVotes(
	data: SubmitVotesRequest
): Promise<SubmitVotesResponse> {
	try {
		const response = await fetch('/api/votes/submit', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(data)
		});

		return await response.json();
	} catch (error) {
		return {
			success: false,
			message: 'Error al conectar con el servidor'
		};
	}
}

export async function getVotingStatistics() {
	try {
		const response = await fetch('/api/votes/stats');
		return await response.json();
	} catch (error) {
		return {
			success: false,
			error: 'Error al obtener estadísticas'
		};
	}
}
