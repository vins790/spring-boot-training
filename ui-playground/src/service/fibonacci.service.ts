export class FibonacciService {
    private static readonly baseUrl = 'http://localhost:8080/api/fib'

    static async getNth(index: number): Promise<number> {
        const response = await fetch(`${FibonacciService.baseUrl}/${index}`);
        if (!response.ok) {
            throw new Error(`Error fetching Fibonacci value: ${response.statusText}`);
        }
        return response.json();
    }
}