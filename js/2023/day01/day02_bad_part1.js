// code edited by https://graphpl.netlify.app/
// set variable 'output' to return data
const numbers = ['zero', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine']

const parseFirst = (line) => {
    let first = -1
    let minIdx = 1000000
    for (let [idx, n] of numbers.entries()) {
        const position = line.indexOf(n)
        if (position != -1) {
            if (position < minIdx) {
                minIdx = position
                first = idx
            }
        }
    }
    const digitIdx = line.search(/\d/) == -1 ? 100000 : line.search(/\d/)
    return digitIdx < minIdx ? line[digitIdx] : first
}

const parseLast = (line) => {
    let last = -1
    let maxIdx = -1
    for (let [idx, n] of numbers.entries()) {
        const position = line.lastIndexOf(n)
        if (position != -1) {
            if (position > maxIdx) {
                maxIdx = position
                last = idx
            }
        }
    }
    const revertLine = line.split("").reverse().join("")
    const digitIdx = revertLine.search(/\d/) == -1 ? -1 : revertLine.length - revertLine.search(/\d/) -1
    return digitIdx > maxIdx ? line[digitIdx] : last
}

const parseCalibration = (line) => {
    let firstDigit = parseFirst(line)
    let lastDigit = parseLast(line)
    return Number(firstDigit + lastDigit)
}

let output = '%data%'.split('\n').map(l=> parseCalibration(l))//.reduce((a,v) => a + v)