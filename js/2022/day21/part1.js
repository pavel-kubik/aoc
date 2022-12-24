// code edited by https://graphpl.netlify.app/
// set variable 'output' to return data
let map = {};

'%data%'.split('\n').forEach(v=> {
    const form = v.split(': ')
    map[form[0]] = form[1];
})

const evaluate = (item) => {
    if (item.indexOf('+') != -1) {
        const v0 = evaluate(map[item.split(' + ')[0]])
        const v1 = evaluate(map[item.split(' + ')[1]]);
        const out = v0 + v1
        console.log('Step ' + item + ': ' + v0 + ' + ' + v1 + ' = ' + out)
        return out;
    } else if (item.indexOf('-') != -1) {
        const v0 = evaluate(map[item.split(' - ')[0]])
        const v1 = evaluate(map[item.split(' - ')[1]]);
        const out = v0 - v1
        console.log('Step ' + item + ': ' + v0 + ' - ' + v1 + ' = ' + out)
        return out;
    } else if (item.indexOf('*') != -1) {
        const v0 = evaluate(map[item.split(' * ')[0]])
        const v1 = evaluate(map[item.split(' * ')[1]]);
        const out = v0 * v1
        console.log('Step ' + item + ': ' + v0 + ' * ' + v1 + ' = ' + out)
        return out;
    } else if (item.indexOf('/') != -1) {
        const v0 = evaluate(map[item.split(' / ')[0]])
        const v1 = evaluate(map[item.split(' / ')[1]]);
        const out = v0 / v1
        console.log('Step ' + item + ': ' + v0 + ' / ' + v1 + ' = ' + out)
        return out;
    } else {
        console.log(' - Number ' + item)
        return Number(item);
    }
}

let output = evaluate(map['root']); // 732898222483128 too high