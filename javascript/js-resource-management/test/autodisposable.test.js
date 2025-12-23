import assert from "node:assert";
import { test, describe, beforeEach } from "node:test";
import AutoDisposable from "../src/autodisposable.js";

describe("AutoDisposable", () => {
	let calls;

	beforeEach(() => {
		calls = [];
	});

	test("should dispose resources with 'dispose' method", async () => {
		const resource = {
			dispose() {
				calls.push("dispose");
			},
		};

		const result = await AutoDisposable.using(() => resource).apply((r) => {
			assert.strictEqual(r, resource);
			return "done";
		});

		assert.strictEqual(result, "done");
		assert.deepStrictEqual(calls, ["dispose"]);
	});

	test("should skip resources without a disposer and log a warning", async () => {
		const originalWarn = console.warn;
		let warned = false;
		console.warn = (msg) => {
			warned = msg.includes("AutoDisposable: Skipping");
		};

		await AutoDisposable.using(() => ({ name: "no-disposer" })).apply((...resources) => {
			assert.strictEqual(resources.length, 0);
		});

		assert.strictEqual(warned, true);
		console.warn = originalWarn;
	});

	test("should dispose multiple resources in reverse order", async () => {
		const order = [];

		const a = {
			async dispose() {
				order.push("a");
			},
		};

		const b = {
			dispose() {
				order.push("b");
			},
		};

		await AutoDisposable.using(
			() => a,
			() => b,
		).apply((...resources) => {
			assert.strictEqual(a, resources.at(0));
			assert.strictEqual(b, resources.at(1));
		});

		assert.deepStrictEqual(order, ["b", "a"]);
	});

	test("should dispose resources even if the callback throws", async () => {
		const resource = {
			dispose() {
				calls.push("dispose");
			},
		};

		await assert.rejects(
			AutoDisposable.using(() => resource).apply((r) => {
				throw new Error("boom");
			}),
			/boom/
		);

		assert.deepStrictEqual(calls, ["dispose"]);
	});
});
