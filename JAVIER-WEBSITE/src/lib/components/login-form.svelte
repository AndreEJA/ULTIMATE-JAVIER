<script lang="ts">
	import { loginJudge } from '$lib/api-client';

	let email = $state('');
	let password = $state('');
	let error = $state('');
	let loading = $state(false);

	let { onLogin } = $props<{
		onLogin: (judge: { id: string; name: string; email: string }) => void;
	}>();

	async function handleLogin(e: Event) {
		e.preventDefault();
		
		if (!email || !password) {
			error = 'Por favor completa todos los campos';
			return;
		}

		loading = true;
		error = '';

		try {
			const result = await loginJudge({ email, password });

			if (!result.success) {
				error = result.error || 'Error al iniciar sesión';
				loading = false;
				return;
			}

			if (result.judge) {
				onLogin(result.judge);
			}
		} catch (err) {
			error = 'Error al iniciar sesión';
		} finally {
			loading = false;
		}
	}
</script>

<div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 p-4">
	<div class="w-full max-w-md border border-slate-700 bg-slate-800 rounded-lg overflow-hidden">
		<div class="p-6 space-y-2 border-b border-slate-700">
			<div class="flex items-center gap-2">
				<div class="w-8 h-8 rounded-lg bg-gradient-to-br from-blue-400 to-cyan-500 flex items-center justify-center">
					<span class="text-white font-bold text-sm">J</span>
				</div>
				<h2 class="text-white font-semibold">JAVIER</h2>
			</div>
			<p class="text-slate-400 text-sm">
				Java-Based Intelligent Election Registry
			</p>
		</div>
		<div class="p-6">
			<form onsubmit={handleLogin} class="space-y-4">
				<div class="space-y-2">
					<label for="email" class="text-sm font-medium text-slate-300">Email del Juez</label>
					<input
						id="email"
						type="email"
						placeholder="tu.email@uam.edu"
						bind:value={email}
						class="w-full px-3 py-2 bg-slate-700 border border-slate-600 text-white placeholder:text-slate-400 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 focus:ring-offset-slate-800"
					/>
				</div>

				<div class="space-y-2">
					<label for="password" class="text-sm font-medium text-slate-300">Contraseña</label>
					<input
						id="password"
						type="password"
						placeholder="••••••••"
						bind:value={password}
						class="w-full px-3 py-2 bg-slate-700 border border-slate-600 text-white placeholder:text-slate-400 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 focus:ring-offset-slate-800"
					/>
				</div>

				{#if error}
					<div class="p-3 rounded-lg bg-red-500/10 border border-red-500/30 text-red-400 text-sm">
						{error}
					</div>
				{/if}

				<button
					type="submit"
					disabled={loading}
					class="w-full px-4 py-2 bg-gradient-to-r from-blue-500 to-cyan-500 text-white font-medium rounded-md hover:from-blue-600 hover:to-cyan-600 disabled:opacity-50 disabled:cursor-not-allowed transition-all"
				>
					{loading ? 'Iniciando...' : 'Iniciar Sesión'}
				</button>
			</form>

			<div class="mt-6 p-4 bg-slate-700/50 rounded-lg border border-slate-600">
				<p class="text-xs text-slate-400 font-semibold mb-2">Credenciales de Demo:</p>
				<p class="text-xs text-slate-300">Email: judge1@uam.edu</p>
				<p class="text-xs text-slate-300">Contraseña: judge123</p>
			</div>
		</div>
	</div>
</div>
