let [mapData, pathData] = '%data%'.split('\n\n');
pathData += 'N' // fix input issue
const path = pathData.match(/\d+[LRN]/g).map(v => { return {
    distance: v.match(/\d+/g)[0],
    turn: v.match(/[LRN]/g)[0]
}});
const map = mapData.split('\n').map(v => v.split(''));
const mapH = map.length;
const mapW = map[0].length;

const subArray = (map, startX, startY) => {
    let out = [];
    for(let i=startY;i<startY+50;i++) {
        let line = [];
        for(let j=startX;j<startX+50;j++) {
            line.push(map[i][j]);
        }
        out.push(line);
    }
    return out;
}

// parse 50x50 sides
const sides = [
    subArray(map, 50, 0),
    subArray(map, 100, 0),
    subArray(map, 50, 50),
    subArray(map, 0, 100),
    subArray(map, 50, 100),
    subArray(map, 0, 150)
];

const nextCoord = (side, x, y, direction) => {
    const d = dir[direction];
    let newX=x + d[1], newY=y + d[0];
    if (newX < 0 || newX >= 50 || newY < 0 || newY >= 50) {
        console.log('EDGE');
        return switchSide(side, x, y, direction);
    }
    return [newY, newX, side, direction];
}

const switchSide = (side, x, y, direction) => {
    const newSide = edgeMap[side][0][direction];
    const newDirection = edgeMap[side][1][direction];
    let newX = x, newY = y;

    if (edgeMap[side][2][direction] === 'id') {
        const d = dir[direction];
        newX += d[1]
        newY += d[0]
        newX = (newX + 50) % 50;
        newY = (newY + 50) % 50;
    } else if (edgeMap[side][2][direction] === 'r90' ||
        edgeMap[side][2][direction] === 'r-90') {
        [newX, newY] = [newY, newX];
    } else if (edgeMap[side][2][direction] === 'r180') {
        newY = 49 - newY;
    } else {
        throw 'Unknown'
    }

    return [newY, newX, newSide, newDirection];
}

const edgeMap = [
    // > v < ^    new dir
    [ [1,2,3,5], [0,1,0,0], ['id', 'id', 'r180', 'r90'] ], // side 0
    [ [4,2,0,5], [2,2,2,3], ['r180', 'r90', 'id', 'id'] ], // side 1
    [ [1,4,3,0], [3,1,1,3], ['r-90', 'id', 'r-90', 'id'] ], // side 2
    [ [4,5,0,2], [0,1,0,0], ['id', 'id', 'r180', 'r90'] ], // side 3
    [ [1,5,3,2], [2,2,2,3], ['r180', 'r90', 'id', 'id'] ], // side 4
    [ [4,1,0,3], [3,1,1,3], ['r-90', 'id', 'r-90', 'id'] ], // side 5
];

const dir = [   // y, x
    [0, 1],  // >
    [1, 0],  // v
    [0, -1], // <
    [-1, 0]  // ^
]

let x=0,y=0,direction=0,side=0;
// on start position already - side 0, x 0, y 0
// process movements
path.forEach(m => {
    console.log('Move ' + JSON.stringify(m) + ' from XY=' + x + ':' + y)
    // move
    for (let i=0;i<m.distance;i++) {
        const [newY, newX, newSide, newDirection] = nextCoord(side, x, y, direction);
        if (sides[newSide][newY][newX] === '#') {
            console.log('Wall E ' + side + ' XY=' + newX + ":" + newY)
            break;
        }
        x = newX;
        y = newY;
        side = newSide;
        direction = newDirection;
        console.log('XY=' + x + ":" + y + ' E ' + side + " direction " + direction);
    }
    // turn
    if (m.turn !== 'N') {
        direction = (direction + (m.turn === 'R' ? 1 : -1) + 4) % 4;
    }
    console.log('Direction=' + direction)
})

console.log('FINAL POINT E ' + side + ' XY=' + x + ":" + y)

1000 * (y+51) + 4 * (x+51) + direction
