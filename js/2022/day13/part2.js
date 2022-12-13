// Created by https://graphpl.netlify.app/

let compare = (l, r) => {
    // console.log("  compare " + l + " " + r);
    if (!Array.isArray(l) && !Array.isArray(r)) {
        return Math.sign(l - r)
    } else if (Array.isArray(l) && Array.isArray(r)) {
        let diffLength = l.length - r.length;
        for (let i = 0; i < Math.min(l.length, r.length); i++) {
            let diff = compare(l[i], r[i])
            if (diff != 0) {
                return diff
            }
        }
        return Math.sign(diffLength)
    } else {
        if (!Array.isArray(l)) {
            return compare([l], r);
        } else {
            return compare(l, [r]);
        }
    }
}

let signal = [[[2]],[[6]]]
'%data%'.split('\n\n').forEach((group, idx) => {
    let [leftInput, rightInput] = group.split('\n')
    let left = eval(leftInput)
    let right = eval(rightInput)
    signal.push(left, right);
})

console.log(signal)
console.log("=====")

signal.sort((a, b) => compare(a, b))

signal = signal.map(i => JSON.stringify(i))

const start = signal.indexOf(JSON.stringify([[2]]))
const end = signal.indexOf(JSON.stringify([[6]]))
console.log(start);
console.log(end);

(start+1)*(end+1)
