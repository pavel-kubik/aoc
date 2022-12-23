// code edited by https://graphpl.netlify.app/
// set variable 'output' to return data
let input = '%data%'.split('\n').map(Number)
const LENGTH =  input.length;

let indexes = input.map((_,i) => i);

const mix = (file, indexes, i) => {
    const v = file.splice(i, 1)[0];
    const newIdx = (i + v) % (LENGTH -1);
    file.splice(newIdx, 0, v);
    const ind = indexes.splice(i, 1)[0];
    indexes.splice(newIdx, 0, ind);
}

const mixFile = (file, indexes) => {
    console.log(file.join(','));
    indexes.forEach((_, i) => {
        const idx = indexes.indexOf(i);
        console.log('Move ' + file[idx])
        mix(file, indexes, idx);
        console.log(file.join(','));
    });
}

mixFile(input, indexes);

const zeroIdx = input.indexOf(0);
const coords = []

coords.push(input[(zeroIdx + 1000) % LENGTH]);
coords.push(input[(zeroIdx + 2000) % LENGTH]);
coords.push(input[(zeroIdx + 3000) % LENGTH]);

let output = coords.reduce((o,v)=>o+v)