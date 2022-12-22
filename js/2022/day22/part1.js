let [mapData, pathData] = '%data%'.split('\n\n');
pathData += 'N' // fix input issue
const path = pathData.match(/\d+[LRN]/g).map(v => { return {
    distance: v.match(/\d+/g)[0],
    turn: v.match(/[LRN]/g)[0]
}});
const map = mapData.split('\n').map(v => v.split(''));
const mapH = map.length;
const mapW = map[0].length;

// align map
map.map(row => {
    while (row.length < mapW) {
        row.push(' ');
    }
})

const nextCoord = (map, x, y, direction) => {
    const d = dir[direction];
    let newX=x, newY=y;
    do {
        newX = (newX + d[1] + mapW) % mapW;
        newY = (newY + d[0] + mapH) % mapH;
    } while (map[newY][newX] === ' ')
    return [newY, newX];
}

const dir = [   // y, x
    [0, 1],  // >
    [1, 0],  // v
    [0, -1], // <
    [-1, 0]  // ^
]

let x=0,y=0,direction=0;
//find start position
while(map[y][x] !== '.') {
    x++
}
// process movements
path.forEach(m => {
    console.log('Move ' + JSON.stringify(m))
    // move
    for (let i=0;i<m.distance;i++) {
        const [newY, newX] = nextCoord(map, x, y, direction);
        if (map[newY][newX] === '#') {
            break;
        }
        x = newX;
        y = newY;
        console.log('XY=' + x + ":" + y)
    }
    // turn
    if (m.turn !== 'N') {
        direction = (direction + (m.turn === 'R' ? 1 : -1) + 4) % 4;
    }
    console.log('Direction=' + direction)
})

1000 * (y+1) + 4 * (x+1) + direction   // 57374 too low => 162186 (missing space in input data)