import { useFibonacciService } from "../../service/fibonacci.service.ts";
import { useRef, useState } from "react";

export const Fibonacci = () => {
  const fibonacciService = useFibonacciService();

  const inputRef = useRef<HTMLInputElement>(null);
  const [fibValue, setFibValue] = useState<number | null>(null);
  const [fibN, setFibN] = useState<number | null>(null);

  const getFibonacci = async () => {
    const n = Number(inputRef?.current?.value);
    setFibN(n);
    if (Number.isNaN(n)) {
      setFibValue(null);
      return;
    }

    const result = await fibonacciService.getNth(n);
    setFibValue(result);
  }

  return <div className="card">
    <h1>Fibonacci Calculator</h1>
    <div className="input-group">
      <input ref={inputRef}/>
      <button onClick={getFibonacci}>
        Get Fibonacci
      </button>
    </div>
    {Number.isFinite(fibValue) && Number.isFinite(fibN) && <p>{`Fib(${fibN}) = ${fibValue}`}</p>}
  </div>
}