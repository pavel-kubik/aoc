// code edited by https://graphpl.netlify.app/
// set variable 'output' to return data
let map = '%data%'.split('\n').map(l=>l.split(''));

const HEIGHT = map.length
const WIDTH = map[0].length

const isEndState = (coords) => coords[0] === HEIGHT-1 && coords[1] === WIDTH -2;

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

const drawBlizzards = (map, blizzards, i) => {
    const mapCopy = map.map(l=>l.slice()).slice();
    blizzards.forEach(bliz => {
        const b = move(bliz, i);
        //console.log(JSON.stringify(bliz) + " => " + JSON.stringify(b))
        mapCopy[b.y][b.x] = b.dir;
    })
    console.log(mapCopy.map(l=>l.join('')).join('\n'))
}

const move = (b, step) => {
    if (b.dir === '<' || b.dir === '>') {
        // horizontal blizzard
        step = step % (WIDTH-2);
        return {
            x: b.dir === '>' ?
                (b.x + step -1 + WIDTH-2) % (WIDTH -2) + 1 :
                (b.x - step -1 + WIDTH-2) % (WIDTH -2) + 1,
            y: b.y,
            dir: b.dir,
        }
    } else {
        step = step % (HEIGHT-2);
        return {
            x: b.x,
            y: b.dir === 'v' ?
                (b.y + step -1 + HEIGHT-2) % (HEIGHT -2) + 1 :
                (b.y - step -1 + HEIGHT-2) % (HEIGHT -2) + 1,
            dir: b.dir,
        }
    }
}

const isFree = (step, coords) => {
    if (coords[0] < 0) return;
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

let queue = []
queue.push([0, [0,1], ''])

map[0][1] = '#' // close entrance to not got out this way

const quality = (a) => a[0]+a[1];
const fastestWayToExit = (c) => Math.abs(HEIGHT-1 -c[0]) + Math.abs(WIDTH-2 - c[1]);

// for(let i=0;i<15;i++) {
//     drawBlizzards(map, blizzards, i)
// }

let visited = new Set()

let i = 0;
let best = 100000;
let cut = [0,0,0];
while(queue.length > 0) {
    if (best === 100000) {
        queue.sort((a,b) => quality(b[1])-quality(a[1]));
    } else {
        queue.sort((a,b) => b[2].length-a[2].length);
    }
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
    if (isEndState(coords)) {
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

let output = best + ' @' + i;
// 264 too low
// 470 too high
// 290 too low
// 446 not