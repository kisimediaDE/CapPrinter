import { CapPrinter } from 'cap-printer';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    CapPrinter.echo({ value: inputValue })
}
