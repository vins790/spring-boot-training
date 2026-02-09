import {useRef, useState} from 'react'
import './App.css'
import {FibonacciService} from "./service/fibonacci.service.ts";

function App() {
    const inputRef = useRef<HTMLInputElement>(null);
    const [fibValue, setFibValue] = useState<number | null>(null);
    const [fibN, setFibN] = useState<number | null>(null);

    const getFibonacci = async () => {
        const n = Number(inputRef?.current?.value);
        setFibN(n);
        if(Number.isNaN(n)) {
            setFibValue(null);
            return;
        }

        const result = await FibonacciService.getNth(n);
        setFibValue(result);
    }

    return (
        <>
            <div className="card">
                <h1>Fibonacci Calculator</h1>
                <div className="input-group">
                    <input ref={inputRef}/>
                    <button onClick={getFibonacci}>
                        Get Fibonacci
                    </button>
                </div>
                {Number.isFinite(fibValue) && Number.isFinite(fibN) && <p>{`Fib(${fibN}) = ${fibValue}`}</p>}
            </div>

        </>
    )
}

export default App
