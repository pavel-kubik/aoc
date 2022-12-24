// code edited by https://graphpl.netlify.app/
// set variable 'output' to return data
let map = '%data%'.split('\n').map(l=>l.split(''));

const HEIGHT = map.length
const WIDTH = map[0].length

const isEndState = (coords, to) => coords[0] === to[0] && coords[1] === to[1];

const directions = [
        [-1,0], // ^
        [0,1],  // >
        [1,0],  // v
        [0,-1], // <
    ];
const stepToChar = (s) => {
    switch(s) {
        case 0: return '^';
        case 1: return '>';
        case 2: return 'v';
        case 3: return '<'
    }
}
let blizzards = []  // at state 0

map.forEach((l,j) => {
    l.forEach((v,i) => {
        if (v !== '.' && v !== '#') {
            blizzards.push({
                x: i,
                y: j,
                dir: v
            })
            map[j][i] = '.'
        }
    })
})

const isFree = (step, coords) => {
    if (coords[0] < 0 || coords[0] > HEIGHT -1) return;
    if (map[coords[0]][coords[1]] !== '.') return;
    return blizzards.every(b=> {
        if (b.dir === '<' || b.dir === '>') {
            // horizontal blizzard
            if (coords[0] !== b.y) return true;
            return b.dir === '>' ?
                (b.x + (step % (WIDTH-2)) -1 + WIDTH-2) % (WIDTH -2) + 1 !== coords[1] :
                (b.x - (step % (WIDTH-2)) -1 + WIDTH-2) % (WIDTH -2) + 1 !== coords[1]
        } else {
            // vertical blizzard
            if (coords[1] !== b.x) return true;
            return b.dir === 'v' ?
                (b.y + (step % (HEIGHT-2)) -1 + HEIGHT-2) % (HEIGHT -2) + 1 !== coords[0] :
                (b.y - (step % (HEIGHT-2)) -1 + HEIGHT-2) % (HEIGHT -2) + 1 !== coords[0]
        }
    });
}

const quality = (a) => a[0]+a[1];
const fastestWayToExit = (c) => Math.abs(HEIGHT-1 -c[0]) + Math.abs(WIDTH-2 - c[1]);

const findfastestWay = (from, to, time)  => {
    let queue = []
    queue.push([time, from, ''])

    let visited = new Set()

    let i = 0;
    let best = 100000;
    let cut = [0,0,0];
    while(queue.length > 0) {
        const state = queue.shift();
        const step = state[0];
        const coords = state[1];
        const way = state[2];
        if (++i % 10000 === 0) {
            console.log('Steps ' + i + ' way ' + best + ' cut ' + cut.join(':') + ' stack ' + queue.length);
            //break;
        }
        if (way.length >= best) {
            cut[0]++;
            continue;
        }
        if (way.length + fastestWayToExit(coords) >= best) {
            cut[1]++;
            continue;
        }
        const visitedKey = 'T' + step + 'C' + coords.join(':');
        if (visited.has(visitedKey)) {
            cut[2]++;
            continue;
        } else {
            visited.add(visitedKey);
        }
        if (isEndState(coords, to)) {
            console.log('Found at step ' + step + ' way ' + way + ' @' + i);
            if (step < best) {
                best = step;
            }
            continue;
        }
        directions.forEach((d,i) => {
            const position = [coords[0] + d[0], coords[1] + d[1]];
            if (isFree(step+1, position)) {
                queue.push([step+1, position, way + stepToChar(i)]);
            }
        });
        if (isFree(step+1, coords)) queue.push([step+1, coords, way + '_']);
    }
    return best
}

const w1 = findfastestWay([0,1], [HEIGHT-1,WIDTH-2], 0)
console.log('There ' + w1)

const w2 = findfastestWay([HEIGHT-1,WIDTH-2], [0,1], w1)
console.log('Back ' + w2)

const w3 = findfastestWay([0,1], [HEIGHT-1,WIDTH-2], w2)
console.log('There again ' + w3)

let output = w3;
