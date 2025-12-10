<script lang="ts">
	interface Representative {
		name: string;
		title: 'Rey' | 'Reina';
		image: string;
	}

	let { representative } = $props<{ representative: Representative }>();

	const titleClass = representative.title === 'Rey' 
		? 'from-blue-500 to-blue-600' 
		: 'from-pink-500 to-rose-600';
</script>

<div class="border border-slate-700 bg-slate-700/50 rounded-lg overflow-hidden hover:border-slate-600 transition-all p-4">
	<div class="flex flex-col items-center gap-3">
		<div class="relative">
			<div class="w-20 h-20 rounded-full bg-gradient-to-br {titleClass} p-0.5">
				<div class="w-full h-full rounded-full bg-slate-800 flex items-center justify-center overflow-hidden">
					<img 
						src={representative.image || "/placeholder.svg"} 
						alt={representative.name}
						class="w-full h-full object-cover"
					/>
				</div>
			</div>
			<div class="absolute -bottom-1 -right-1 bg-gradient-to-br {titleClass} text-white text-xs font-bold px-2 py-1 rounded-full">
				{representative.title}
			</div>
		</div>
		<div class="text-center">
			<p class="text-white font-semibold text-sm">{representative.name}</p>
			<p class="text-slate-400 text-xs">{representative.title} 2025</p>
		</div>
	</div>
</div>
