export interface Judge {
	id: string;
	email: string;
	name: string;
	password_hash: string;
	created_at: Date;
}

export interface Vote {
	id: string;
	judge_id: string;
	faculty_name: string;
	category_id: string;
	score: number;
	created_at: Date;
}

export interface VotingSession {
	id: string;
	judge_id: string;
	started_at: Date;
	completed_at: Date | null;
	total_votes: number;
}

// Simulación de base de datos en memoria para desarrollo
export const judges: Map<string, Judge> = new Map([
	[
		'judge1@uam.edu',
		{
			id: '1',
			email: 'judge1@uam.edu',
			name: 'Dr. Carlos García',
			password_hash: 'hashed_judge123',
			created_at: new Date()
		}
	],
	[
		'judge2@uam.edu',
		{
			id: '2',
			email: 'judge2@uam.edu',
			name: 'Dra. María López',
			password_hash: 'hashed_judge123',
			created_at: new Date()
		}
	],
	[
		'judge3@uam.edu',
		{
			id: '3',
			email: 'judge3@uam.edu',
			name: 'Prof. Juan Rodríguez',
			password_hash: 'hashed_judge123',
			created_at: new Date()
		}
	]
]);

export const votes: Vote[] = [];
export const sessions: Map<string, VotingSession> = new Map();
