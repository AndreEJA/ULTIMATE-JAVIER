<script lang="ts">
	import VotingForm from './voting-form.svelte';
	import ResultsDashboard from './results-dashboard.svelte';

	interface Judge {
		id: string;
		name: string;
		email: string;
	}

	let { judge, onLogout, onVotesSubmitted } = $props<{
		judge: Judge;
		onLogout: () => void;
		onVotesSubmitted: (votes: Record<string, Record<string, number>>) => void;
	}>();

	let showVotingForm = $state(false);
	let votesSubmitted = $state(false);
	let submittedVotes = $state<Record<string, Record<string, number>> | null>(null);

	function handleVotesSubmit(votes: Record<string, Record<string, number>>) {
		submittedVotes = votes;
		votesSubmitted = true;
		showVotingForm = false;
		onVotesSubmitted(votes);
	}

	function handleStartNewVoting() {
		votesSubmitted = false;
		submittedVotes = null;
		showVotingForm = true;
	}
</script>

<div class="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900">
	<header class="border-b border-slate-700 bg-slate-800/50 backdrop-blur sticky top-0 z-50">
		<div class="max-w-7xl mx-auto px-4 py-4 flex items-center justify-between">
			<div class="flex items-center gap-3">
				<div class="w-10 h-10 rounded-lg bg-gradient-to-br from-blue-400 to-cyan-500 flex items-center justify-center">
					<span class="text-white font-bold">J</span>
				</div>
				<div>
					<h1 class="text-white font-bold">JAVIER</h1>
					<p class="text-xs text-slate-400">Election Registry</p>
				</div>
			</div>

			<div class="flex items-center gap-4">
				<div class="text-right">
					<p class="text-sm text-white font-medium">{judge.name}</p>
					<p class="text-xs text-slate-400">{judge.email}</p>
				</div>
				<button
					onclick={onLogout}
					class="px-4 py-2 border border-slate-600 text-slate-300 rounded-md hover:bg-slate-700 transition-colors"
				>
					Cerrar Sesión
				</button>
			</div>
		</div>
	</header>

	<main class="max-w-7xl mx-auto px-4 py-8">
		{#if votesSubmitted && submittedVotes}
			<ResultsDashboard 
				votes={submittedVotes}
				onNewVoting={handleStartNewVoting}
			/>
		{:else if showVotingForm}
			<VotingForm
				{judge}
				onBack={() => (showVotingForm = false)}
				onSubmit={handleVotesSubmit}
			/>
		{:else}
			<div class="mb-8 border border-slate-700 bg-slate-800 rounded-lg overflow-hidden">
				<div class="absolute inset-0 bg-gradient-to-r from-blue-500/10 to-cyan-500/10" />
				<div class="relative p-6 border-b border-slate-700">
					<h2 class="text-2xl font-semibold text-white">Bienvenido, {judge.name}</h2>
					<p class="text-slate-400 text-sm">
						Realiza tu votación para el Reinado UAM 2025
					</p>
				</div>
				<div class="relative p-6">
					<p class="text-slate-300 mb-6">
						Selecciona las facultades de la Universidad Autónoma de México y asigna puntuaciones del 0 al 100 en cada categoría de evaluación.
					</p>
					<button
						onclick={() => (showVotingForm = true)}
						class="px-6 py-2 bg-gradient-to-r from-blue-500 to-cyan-500 text-white font-medium rounded-md hover:from-blue-600 hover:to-cyan-600 transition-all"
					>
						Comenzar Votación
					</button>
				</div>
			</div>

			<div class="border border-slate-700 bg-slate-800/50 rounded-lg overflow-hidden">
				<div class="p-6 border-b border-slate-700">
					<h3 class="text-slate-300 font-semibold">Categorías de Evaluación</h3>
				</div>
				<div class="p-6 grid grid-cols-1 md:grid-cols-2 gap-4">
					<div class="flex gap-3">
						<div class="w-8 h-8 rounded-full bg-blue-500/20 flex items-center justify-center flex-shrink-0 text-blue-400 text-sm font-bold">1</div>
						<div>
							<p class="text-white font-medium">Fashion Show</p>
							<p class="text-slate-400 text-sm">30% de la puntuación total</p>
						</div>
					</div>
					<div class="flex gap-3">
						<div class="w-8 h-8 rounded-full bg-cyan-500/20 flex items-center justify-center flex-shrink-0 text-cyan-400 text-sm font-bold">2</div>
						<div>
							<p class="text-white font-medium">Discurso</p>
							<p class="text-slate-400 text-sm">10% de la puntuación total</p>
						</div>
					</div>
					<div class="flex gap-3">
						<div class="w-8 h-8 rounded-full bg-emerald-500/20 flex items-center justify-center flex-shrink-0 text-emerald-400 text-sm font-bold">3</div>
						<div>
							<p class="text-white font-medium">Presentación Personal</p>
							<p class="text-slate-400 text-sm">25% de la puntuación total</p>
						</div>
					</div>
					<div class="flex gap-3">
						<div class="w-8 h-8 rounded-full bg-purple-500/20 flex items-center justify-center flex-shrink-0 text-purple-400 text-sm font-bold">4</div>
						<div>
							<p class="text-white font-medium">Respuestas</p>
							<p class="text-slate-400 text-sm">20% de la puntuación total</p>
						</div>
					</div>
					<div class="flex gap-3">
						<div class="w-8 h-8 rounded-full bg-pink-500/20 flex items-center justify-center flex-shrink-0 text-pink-400 text-sm font-bold">5</div>
						<div>
							<p class="text-white font-medium">Carisma</p>
							<p class="text-slate-400 text-sm">15% de la puntuación total</p>
						</div>
					</div>
				</div>
			</div>
		{/if}
	</main>
</div>
