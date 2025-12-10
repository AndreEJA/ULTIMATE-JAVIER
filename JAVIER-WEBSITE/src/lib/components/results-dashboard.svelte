<script lang="ts">

	let { votes, onNewVoting } = $props<{
		votes: Record<string, Record<string, number>>;
		onNewVoting: () => void;
	}>();

	const categoryWeights: Record<string, number> = {
		fashion: 30,
		discurso: 10,
		personal: 25,
		respuestas: 20,
		carisma: 15
	};

	const categoryNames: Record<string, string> = {
		fashion: 'Fashion Show',
		discurso: 'Discurso',
		personal: 'Presentación Personal',
		respuestas: 'Respuestas',
		carisma: 'Carisma'
	};

	function calculateFacultyScore(facultyVotes: Record<string, number>): number {
		let totalScore = 0;
		let totalWeight = 0;

		for (const [categoryId, score] of Object.entries(facultyVotes)) {
			if (categoryWeights[categoryId]) {
				totalScore += score * (categoryWeights[categoryId] / 100);
				totalWeight += categoryWeights[categoryId] / 100;
			}
		}

		return totalWeight > 0 ? Math.round(totalScore) : 0;
	}

	const facultyRanking = Object.entries(votes)
		.map(([faculty, facultyVotes]) => ({
			faculty,
			score: calculateFacultyScore(facultyVotes),
			votes: facultyVotes
		}))
		.sort((a, b) => b.score - a.score);

	function getScoreColor(score: number): string {
		if (score < 40) return 'from-red-500 to-red-600';
		if (score < 60) return 'from-yellow-500 to-yellow-600';
		if (score < 80) return 'from-blue-500 to-blue-600';
		return 'from-emerald-500 to-emerald-600';
	}
</script>

<div class="space-y-6">
	<div class="border border-emerald-700 bg-emerald-900/20 rounded-lg p-6">
		<div class="flex items-center gap-3">
			<div class="w-12 h-12 rounded-full bg-emerald-500/20 flex items-center justify-center">
				<span class="text-2xl">✓</span>
			</div>
			<div>
				<p class="text-emerald-400 font-semibold">¡Votación completada exitosamente!</p>
				<p class="text-emerald-300/80 text-sm">Tus votos han sido registrados y están siendo procesados.</p>
			</div>
		</div>
	</div>

	<div class="grid grid-cols-1 md:grid-cols-3 gap-4">
		<div class="border border-slate-700 bg-slate-800 rounded-lg p-6">
			<p class="text-slate-400 text-sm font-medium mb-2">Total Facultades</p>
			<p class="text-3xl font-bold text-blue-400">{Object.keys(votes).length}</p>
		</div>
		<div class="border border-slate-700 bg-slate-800 rounded-lg p-6">
			<p class="text-slate-400 text-sm font-medium mb-2">Total Categorías</p>
			<p class="text-3xl font-bold text-cyan-400">{Object.keys(votes[Object.keys(votes)[0]] || {}).length}</p>
		</div>
		<div class="border border-slate-700 bg-slate-800 rounded-lg p-6">
			<p class="text-slate-400 text-sm font-medium mb-2">Puntuación Promedio</p>
			<p class="text-3xl font-bold text-purple-400">
				{Math.round(facultyRanking.reduce((sum, f) => sum + f.score, 0) / facultyRanking.length)}
			</p>
		</div>
	</div>

	<div class="border border-slate-700 bg-slate-800 rounded-lg overflow-hidden">
		<div class="p-6 border-b border-slate-700">
			<h3 class="text-white font-semibold">Ranking de Facultades</h3>
		</div>
		<div class="p-6 space-y-3">
			{#each facultyRanking as ranking, index}
				<div class="flex items-center gap-4 p-4 rounded-lg bg-slate-700/50 hover:bg-slate-700 transition-colors">
					<div class="w-8 h-8 rounded-full bg-gradient-to-br {getScoreColor(ranking.score)} flex items-center justify-center flex-shrink-0">
						<span class="text-white font-bold text-sm">#{index + 1}</span>
					</div>
					<div class="flex-1">
						<p class="text-white font-semibold">{ranking.faculty}</p>
						<p class="text-slate-400 text-sm">Puntuación Total</p>
					</div>
					<div class="text-right">
						<p class="text-2xl font-bold text-white">{ranking.score}</p>
						<p class="text-xs text-slate-400">/ 100</p>
					</div>
				</div>
			{/each}
		</div>
	</div>

	<div class="border border-slate-700 bg-slate-800 rounded-lg overflow-hidden">
		<div class="p-6 border-b border-slate-700">
			<h3 class="text-white font-semibold">Desglose por Categoría</h3>
		</div>
		<div class="p-6 space-y-4">
			{#each Object.entries(categoryNames) as [categoryId, categoryName]}
				<div>
					<div class="flex items-center justify-between mb-2">
						<span class="text-white font-medium">{categoryName}</span>
						<span class="text-slate-400 text-sm">({categoryWeights[categoryId]}%)</span>
					</div>
					<div class="space-y-1">
						{#each facultyRanking as ranking}
							<div class="flex items-center justify-between text-sm">
								<span class="text-slate-400">{ranking.faculty}</span>
								<span class="text-white font-semibold">{ranking.votes[categoryId]}</span>
							</div>
						{/each}
					</div>
				</div>
				<div class="h-px bg-slate-700" />
			{/each}
		</div>
	</div>

	<div class="flex gap-3 justify-center">
		<button
			onclick={onNewVoting}
			class="px-6 py-2 bg-gradient-to-r from-blue-500 to-cyan-500 text-white font-medium rounded-md hover:from-blue-600 hover:to-cyan-600 transition-all"
		>
			Realizar Otra Votación
		</button>
	</div>
</div>
