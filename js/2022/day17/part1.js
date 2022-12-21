const steams = '%data%'.split('\n').join();

const tiles = ['-','+', 'L', 'I', 'o']

const shapes = {
    '-': ['####'],
    '+': ['.#.', '###', '.#.'],
    'L': ['..#', '..#', '###'],
    'I': ['#', '#', '#', '#'],
    'o': ['##', '##']
}

const isNextMoveFree = (shape, dXY, tileXY, chamber) => {
    return shape.every((line,j) => {
        return line.split('').every((v,i) => {
            if (v !== '.' &&
                chamber[tileXY[1] - j + dXY[1]][tileXY[0] + i + dXY[0]] !== '.') {
                return false;
            }
            return true
        })
    })
}

const fixTileInChamber = (shape, tileXY, chamber, pattern) => {
    shape.forEach((line,j) => {
        line.split('').forEach((v,i) => {
            if (v !== '.') {
                chamber[tileXY[1] - j][tileXY[0] + i] = pattern;
            }
        })
    })
}

let chamber = Array.from(Array(10), ()  => '|.......|'.split(''));
chamber.unshift('---------'.split(''))

console.log(chamber);

let top = 0;
let tilesIdx = 0;
let tile = tiles[tilesIdx % tiles.length];
let shape = shapes[tile];
let tileXY;// = [3, top + tile.leght + 3];
let newTile = true;
let i = 0;
let landed = 1;
while(true) {
    if (newTile) {
        console.log('New tile @' + i + ' with TOP=' + top);
        tile = tiles[tilesIdx++ % tiles.length];
        shape = shapes[tile];
        const newY = top + shape.length + 3;
        while (newY > chamber.length - 1) {
            chamber.push('|.......|'.split(''));
        }
        tileXY = [3, newY];
        newTile = false;

        if (!newTile) fixTileInChamber(shape, tileXY, chamber, '@');
        console.log(chamber.slice().reverse().map(line => line.join('')).join('\n'));
        if (!newTile) fixTileInChamber(shape, tileXY, chamber, '.');
    }
    //console.log('=== Step ' + i + ' tile \'' + tile + '\' position ' + tileXY.join(':'));
    // steam push
    const steamDirection = steams[i % steams.length] === '>' ? 1 : -1;
    //console.log('Push ' + steams[i % steams.length]);
    if (isNextMoveFree(shape, [steamDirection, 0], tileXY, chamber)) {
        tileXY[0] += steams[i % steams.length] === '>' ? 1 : -1;
        //console.log(' ..pushed');
    }
    // falling
    if (isNextMoveFree(shape, [0, -1], tileXY, chamber)) {
        tileXY[1]--;
        // if (!isNextMoveFree(shape, [0, -1], tileXY, chamber)) {
        //     fixTileInChamber(shape, tileXY, chamber, '#');
        //     top = tileXY[1];
        //     newTile = true;
        // }
    } else {
        //console.log('Landed');
        fixTileInChamber(shape, tileXY, chamber, '#');
        top = Math.max(top, tileXY[1]);
        newTile = true;
        if (++landed > 2022) {
            break;
        }
    }
    i++
}
top // not 3118 low, 3121 low, 3124