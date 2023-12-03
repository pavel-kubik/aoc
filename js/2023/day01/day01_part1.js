// code edited by https://graphpl.netlify.app/
// set variable 'output' to return data
const parseCalibration = (line) => {
    let firstDigit = line.search(/\d/)
    let revLine = line.split("").reverse().join("")
    let lastDigit = revLine.search(/\d/)
    return Number(line[firstDigit] + revLine[lastDigit])
}

let output = '%data%'.split('\n').map(l=> parseCalibration(l)).reduce((a,v) => a + v)