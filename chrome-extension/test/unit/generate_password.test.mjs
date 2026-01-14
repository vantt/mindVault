
import assert from 'assert';
import { GeneratePassword } from '../../src/core/usecases/generate_password.js';
import { RegexParserAdapter } from '../../src/adapters/infrastructure/regex_parser_adapter.js';

class MockStorage {
    constructor(secrets = {}) {
        this.secrets = secrets;
    }
    async getSecret(index) {
        return this.secrets[index] || null;
    }
    async isAuthenticated() { return true; }
}

async function runTests() {
    const parser = new RegexParserAdapter();
    const secrets = {
        1: "Basic*",
        2: "Secure#",
        3: "Trade&",
        4: "Ultra$"
    };
    const storage = new MockStorage(secrets);
    const useCase = new GeneratePassword(parser, storage);

    console.log("Running GeneratePassword Tests...");

    // Test 1: Prefix (#)
    // "r4nd0m" + "#1" + "Basic*" -> "Basic*r4nd0m"
    try {
        const result = await useCase.execute("r4nd0m#1");
        assert.strictEqual(result, "Basic*r4nd0m");
        console.log("PASS: Prefix (#)");
    } catch (e) {
        console.error("FAIL: Prefix (#)", e);
    }

    // Test 2: Suffix ($)
    // "r4nd0m$1" -> "r4nd0mBasic*"
    try {
        const result = await useCase.execute("r4nd0m$1");
        assert.strictEqual(result, "r4nd0mBasic*");
        console.log("PASS: Suffix ($)");
    } catch (e) {
        console.error("FAIL: Suffix ($)", e);
    }

    // Test 3: Middle (@)
    // "r4nd0m@2" + "Secure#" (2=Secure#) -> "r4nSecure#d0m"
    try {
        const result = await useCase.execute("r4nd0m@2");
        assert.strictEqual(result, "r4nSecure#d0m");
        console.log("PASS: Middle (@)");
    } catch (e) {
        console.error("FAIL: Middle (@)", e);
    }

    // Test 4: Modifier Reverse Position (_)
    // "r4nd0m#1_" -> Suffix -> "r4nd0mBasic*"
    try {
        const result = await useCase.execute("r4nd0m#1_");
        assert.strictEqual(result, "r4nd0mBasic*");
        console.log("PASS: Modifier Reverse Position (_)");
    } catch (e) {
        console.error("FAIL: Modifier Reverse Position (_)", e);
    }

    // Test 5: Modifier Uppercase (!)
    // "r4nd0m#1!" -> Secret "BASIC*" -> "BASIC*r4nd0m"
    try {
        const result = await useCase.execute("r4nd0m#1!");
        assert.strictEqual(result, "BASIC*r4nd0m");
        console.log("PASS: Modifier Uppercase (!)");
    } catch (e) {
        console.error("FAIL: Modifier Uppercase (!)", e);
    }
    
    // Test 6: Modifier Remove Special (~)
    // "r4nd0m#3~" -> Secret "Trade&" -> "Trade" -> "Trader4nd0m"
    // Wait, #3 is Trade&. # implies Prefix. So "Trader4nd0m".
    try {
        const result = await useCase.execute("r4nd0m#3~");
        assert.strictEqual(result, "Trader4nd0m");
        console.log("PASS: Modifier Remove Special (~)");
    } catch (e) {
        console.error("FAIL: Modifier Remove Special (~)", e);
    }

    // Test 7: Modifier Reverse Secret (?)
    // "r4nd0m#1?" -> Secret "Basic*" -> "*cisaB" -> "*cisaBr4nd0m"
    try {
        const result = await useCase.execute("r4nd0m#1?");
        assert.strictEqual(result, "*cisaBr4nd0m");
        console.log("PASS: Modifier Reverse Secret (?)");
    } catch (e) {
        console.error("FAIL: Modifier Reverse Secret (?)", e);
    }

    // Test 8: Multiple Modifiers (_!)
    // "r4nd0m#1_!" -> Reverse Position (Suffix) + Uppercase -> "r4nd0mBASIC*"
    try {
        const result = await useCase.execute("r4nd0m#1_!");
        assert.strictEqual(result, "r4nd0mBASIC*");
        console.log("PASS: Multiple Modifiers (_!)");
    } catch (e) {
        console.error("FAIL: Multiple Modifiers (_!)", e);
    }

    // Test 9: Interleave (%)
    // "r4nd0m%3" + "Trade&" -> r+T, 4+r, n+a, d+d, 0+e, m+& -> "rT4rnadd0em&"
    try {
        const result = await useCase.execute("r4nd0m%3");
        assert.strictEqual(result, "rT4rnadd0em&");
        console.log("PASS: Interleave (%)");
    } catch (e) {
        console.error("FAIL: Interleave (%)", e);
    }

    // Test 10: Version Handling
    // "r4nd0m#1_v2" -> Secret "Basic*" -> "Basic*_v2" -> "Basic*_v2r4nd0m"
    try {
        const result = await useCase.execute("r4nd0m#1_v2");
        assert.strictEqual(result, "Basic*_v2r4nd0m");
        console.log("PASS: Version Handling (_v2)");
    } catch (e) {
        console.error("FAIL: Version Handling (_v2)", e);
    }
}

runTests();
