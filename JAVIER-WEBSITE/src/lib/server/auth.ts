import { judges } from './db';

export interface AuthCredentials {
	email: string;
	password: string;
}

export interface AuthResponse {
	success: boolean;
	judge?: {
		id: string;
		name: string;
		email: string;
	};
	error?: string;
}

export function authenticateJudge(credentials: AuthCredentials): AuthResponse {
	const judge = judges.get(credentials.email);

	if (!judge) {
		return {
			success: false,
			error: 'Juez no encontrado'
		};
	}

	// En desarrollo simple, comparar directamente
	// En producción, usar bcrypt o similar
	if (credentials.password !== 'judge123') {
		return {
			success: false,
			error: 'Contraseña incorrecta'
		};
	}

	return {
		success: true,
		judge: {
			id: judge.id,
			name: judge.name,
			email: judge.email
		}
	};
}

export function validateJudgeSession(judgeId: string): boolean {
	return judges.values().some((judge) => judge.id === judgeId);
}
