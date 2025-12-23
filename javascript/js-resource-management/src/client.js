import Valkey from "iovalkey";
import AutoDisposable from "./autodisposable.js";

export default async function check(key, value) {
	return await AutoDisposable
		.using(() => ({
			client: new Valkey(),
			async dispose() {
				this.client.disconnect();
			}
		}))
		.apply(async ({ client }) => {
			await client.set(key, value);
			return await client.get(key);
		});
}
