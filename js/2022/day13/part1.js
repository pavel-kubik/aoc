// Created by https://graphpl.netlify.app/

let compare = (l, r) => {
    console.log("  compare " + l + " " + r);
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

let sum = 0
'%data%'.split('\n\n').forEach((group, idx) => {
    let [leftInput, rightInput] = group.split('\n')
    let left = eval(leftInput)
    let right = eval(rightInput)
    console.log(left)
    console.log(right)
    let out = compare(left, right)
    if (out === -1) {
        sum += (idx+1)
    }
    console.log('compare: ' + compare(left, right));
    console.log('===')
})
sum
