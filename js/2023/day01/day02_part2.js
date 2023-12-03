// code edited by https://graphpl.netlify.app/
// set variable 'output' to return data
const numbers = ['one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine']

const parseCalibration = (line) => {
    numbers.forEach((n, idx) => line = line.replaceAll(n, `${n}${idx + 1}${n}`))
    let firstDigit = line.search(/\d/)
    let revLine = line.split("").reverse().join("")
    let lastDigit = revLine.search(/\d/)
    return Number(line[firstDigit] + revLine[lastDigit])
}

let output = '%data%'.split('\n').map(l=> parseCalibration(l)).reduce((a,v) => a + v)