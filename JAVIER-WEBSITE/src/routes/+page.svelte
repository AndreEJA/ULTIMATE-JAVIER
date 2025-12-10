<script lang="ts">
	import LoginForm from '$lib/components/login-form.svelte';
	import VotingDashboard from '$lib/components/voting-dashboard.svelte';
	import { writable } from 'svelte/store';

	let isLoggedIn = $state(false);
	let currentJudge = $state<{
		id: string;
		name: string;
		email: string;
	} | null>(null);

	let completedVotes = $state<Record<string, Record<string, Record<string, number>>>>({});

	function handleLogin(judge: { id: string; name: string; email: string }) {
		currentJudge = judge;
		isLoggedIn = true;
	}

	function handleLogout() {
		isLoggedIn = false;
		currentJudge = null;
	}

	function handleVotesSubmitted(votes: Record<string, Record<string, number>>) {
		if (currentJudge) {
			completedVotes[currentJudge.id] = votes;
		}
	}
</script>

<div class="min-h-screen bg-background">
	{#if isLoggedIn && currentJudge}
		<VotingDashboard 
			judge={currentJudge} 
			onLogout={handleLogout}
			onVotesSubmitted={handleVotesSubmitted}
		/>
	{:else}
		<LoginForm onLogin={handleLogin} />
	{/if}
</div>
