// code edited by https://graphpl.netlify.app/
// set variable 'output' to return data
let map = {};

'%data%'.split('\n').forEach(v=> {
    const form = v.split(': ')
    map[form[0]] = form[1];
})

const evaluate = (item) => {
    if (item.indexOf('=') != -1) {
        const v0 = evaluate(map[item.split(' = ')[0]])
        const v1 = evaluate(map[item.split(' = ')[1]]);
        const out = v0 === v1
        //console.log('Step ' + item + ': ' + v0 + ' === ' + v1 + ' = ' + (v0-v1))
        return out;
    } else if (item.indexOf('+') != -1) {
        const v0 = evaluate(map[item.split(' + ')[0]])
        const v1 = evaluate(map[item.split(' + ')[1]]);
        const out = v0 + v1
        return out;
    } else if (item.indexOf('-') != -1) {
        const v0 = evaluate(map[item.split(' - ')[0]])
        const v1 = evaluate(map[item.split(' - ')[1]]);
        const out = v0 - v1
        return out;
    } else if (item.indexOf('*') != -1) {
        const v0 = evaluate(map[item.split(' * ')[0]])
        const v1 = evaluate(map[item.split(' * ')[1]]);
        const out = v0 * v1
        return out;
    } else if (item.indexOf('/') != -1) {
        const v0 = evaluate(map[item.split(' / ')[0]])
        const v1 = evaluate(map[item.split(' / ')[1]]);
        const out = v0 / v1
        return out;
    } else {
        return Number(item);
    }
}

// 2796

map['root'] = map['root'].replace('+', '=');
let i;
const INT = 47140000;   
// 3558714850968
// 3558715077444
for(i=3558714850968;i<3558715077444;i++) {
    map['humn'] = '' + i//(2796*(27*i+1));
    if (evaluate(map['root'])) {
        console.log('Humn: ' +  map['humn']);
        break;
    }
}

let output = map['humn']