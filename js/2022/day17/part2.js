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
    //chamber[tileXY[1]].push(landed + ',');
}

let chamber = Array.from(Array(10), ()  => '|.......|'.split(''));
chamber.unshift('---------'.split(''))

console.log(steams.length)

const pattern = `|#####..|
|...###.|
|...####|
|.#####.|
|#####..|
|..####.|
|..####.|
|..####.|
|.###...|
|.##....|
|.#####.|
|.##....|
|.##.##.|
|######.|`

const patternTest = `|#####..|
|#.#....|
|#.#....|
|####...|
|..#####|
|...#.##|
|..####.|
|.##....|
|.##...#|
|..#...#|
|..#.###|
|..#..#.|
|..#.###|
|.#####.|`

const matchPattern = (pattern, chamber, top, offset) => {
    const chamberTxt = chamber.map(line => line.join(''));
    return pattern.split('\n').every((line, j) => {
        return line === chamberTxt[top-offset-j]
    });
}

const matchPatternWindow = (pattern, chamber, top, window) => {
    for(let i=0;i<=window;i++) {
        if (matchPattern(pattern, chamber, top, i)) {
            return true;
        }
    }
    return false;
}

const TARGET = 1000000000000;

let top = 0;
let tilesIdx = 0;
let tile = tiles[tilesIdx % tiles.length];
let shape = shapes[tile];
let tileXY;// = [3, top + tile.leght + 3];
let newTile = true;
let i = 0;
let landed = 1;
let first=null, second=null;
let topFirst, topSecond;
let topSkip = 0;
while(true) {
    if (newTile) {
        //console.log('New tile @' + i + ' with TOP=' + top);
        tile = tiles[tilesIdx++ % tiles.length];
        shape = shapes[tile];
        const newY = top + shape.length + 3;
        while (newY > chamber.length - 1) {
            chamber.push('|.......|'.split(''));
        }
        tileXY = [3, newY];
        newTile = false;
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
        if (top > 1908) {
            if(second == null && matchPatternWindow(pattern, chamber, top, 5)) {
                console.log('Patern found @' + landed + ' with top ' + top);
                if (first == null) {
                    first = landed;
                    topFirst = top;
                } else if (second == null && top != topFirst) {
                    topSecond = top;
                    second = landed;
                    
                    // do skip under 1000000000000
                    const period = second - first;
                    const cycles = Math.floor((TARGET - landed)/period);
                    console.log('Cycles to skip ' + cycles);
                    landed += period*cycles;
                    console.log('Landed ' + landed);
                    topSkip = (topSecond - topFirst)*cycles;
                    console.log('Top skip ' + topSkip);
                }
            }
        }
        if (++landed > TARGET) {
            // if (!newTile) fixTileInChamber(shape, tileXY, chamber, '@');
            // console.log(chamber.slice().reverse().map(line => line.join('')).join('\n'));
            // if (!newTile) fixTileInChamber(shape, tileXY, chamber, '.');
            console.log('All landed');
            break;
        }
    }
    i++
}
top+topSkip // chamber period = 2654