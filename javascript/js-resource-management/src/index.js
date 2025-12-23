import check from "./client.js";

try {
	const maxClients = 7;
	for (let i = 1; i <= maxClients; i++) {
		const key = "key" + i;
		const value = "value" + i;
		console.log(await check(key, value));
	}
} catch (error) {
	console.error(String(error));
	process.exit(1);
}
