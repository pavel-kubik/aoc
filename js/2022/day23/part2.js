// code edited by https://graphpl.netlify.app/
// set variable 'output' to return data
let map = '%data%'.split('\n').map(line => line.split(''));

let elves = [];

const neighbours = [    // y, x
    [-1,-1],
    [-1,0],
    [-1,1],
    [0,-1],
    [0,1],
    [1,-1],
    [1,0],
    [1,1]
]

const north = [[-1,-1],[-1,0],[-1,1]];  // y, x
const south = [[1,-1],[1,0],[1,1]];
const west = [[-1,-1],[0,-1],[1,-1]];
const east = [[-1,1],[0,1],[1,1]];

const dir = {
    'N': [-1,0],
    'S': [1,0],
    'W': [0,-1],
    'E': [0,1]
}

const extendMap = (map) => {
    const size = map[0].length;
    map.forEach(r=>r.push('.') && r.unshift('.'));
    map.unshift(Array(size+2).fill('.'));
    map.push(Array(size+2).fill('.'));
}

const emptySurround = (map, e, s) => s.every(n => map[e.y + n[0]][e.x + n[1]] === '.');

const proposeMove = (map, e) => {
    if (emptySurround(map, e, neighbours)) {
        e.move = '-';
    } else {
        const found = e.priority.some(p => {
            switch(p) {
                case 'N':
                    if (emptySurround(map, e, north)) {
                        e.move = p;
                        return true;
                    }
                    return false;
                case 'S':
                    if (emptySurround(map, e, south)) {
                        e.move = p;
                        return true;
                    }
                    return false;               
                case 'W':
                    if (emptySurround(map, e, west)) {
                        e.move = p;
                        return true;
                    }
                    return false;
                case 'E':
                    if (emptySurround(map, e, east)) {
                        e.move = p;
                        return true;
                    }
                    return false;
            }
        })
        if (!found) {
            e.move = '-';
        }
    }
    const d = e.priority.shift();
    e.priority.push(d);           
}

map.forEach((r,y) => {
    r.forEach((c,x) => {
        if (c === '#') {
            elves.push({
                x: x,
                y: y,
                priority: ['N', 'S', 'W', 'E']
            });
        }
    })
})

let i;
for(i=0;i<1000;i++) {
    console.log("Round " + (i + 1))

    let minX=1000,minY=1000,maxX=0,maxY=0;
    elves.forEach(e => {
        if (e.x < minX) minX = e.x
        if (e.y < minY) minY = e.y
        if (e.x > maxX) maxX = e.x
        if (e.y > maxY) maxY = e.y
    })
    if (minX < 1 || minY < 1 || maxY > map.length-2 || maxX > map[0].length-2) {
        extendMap(map)
        //remapp elves coordinates
        elves.forEach(e => {
            e.x++;
            e.y++;
        });
    }
    // find propsed move
    elves.forEach(e => proposeMove(map, e));
    // remove collisions
    let newPositions = new Set();
    let collisions = new Set();
    elves.forEach(e => {
        if (e.move !== '-') {
            const dxy = dir[e.move];
            e.newY = e.y + dxy[0]
            e.newX = e.x + dxy[1];
            const coord = [e.newY, e.newX].join(':');
            if (newPositions.has(coord)) {
                collisions.add(coord);
            } else {
                newPositions.add(coord);
            }
        }
    });
    collisions.forEach(col => {
        const c = col.split(':').map(Number);
        elves.forEach(e => {
            if (e.newX === c[1] && e.newY === c[0]) {
                e.move = '-';
            }
        })
    })
    // move elves
    let moved = 0
    elves.forEach(e => {
        if (e.move !== '-') {
            map[e.y][e.x] = '.'
            map[e.newY][e.newX] = '#'
            e.x = e.newX;
            e.y = e.newY;
            moved++;
        }
    })
    if (!moved) {
        console.log('No movement')
        break;
    }
    //console.log(map.map(l=>l.join('')).join('\n'));
}

let output = i+1
// 15231 too hight
