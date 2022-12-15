let sum = 0;
let rockLines = []
'%data%'.split('\n').forEach((line) => {
    rockLines.push(line.split(' -> ').map(x => x.split(',')))
})

let minX=1000, minY=1000, maxX=0, maxY=0
rockLines.forEach(l => {
    l.forEach(p => {
        if (p[0] < minX) minX = Number(p[0])
        if (p[1] < minY) minY = Number(p[1])
        if (p[0] > maxX) maxX = Number(p[0])
        if (p[1] > maxY) maxY = Number(p[1])
    })
})
console.log(minX + ':' + minY + " -> " + maxX + ":" + maxY)

// shift all by minX - 1, minY
const offsetX = minX-1;
const offsetY = minY-1;
rockLines = rockLines.map(l => l.map(p => [p[0] - offsetX, p[1] - 0]));

let cave = Array(maxY + 2).fill().map(() => Array(maxX-minX+3).fill('.'));
//console.log(cave);

const drawLine = (start, end) => {
    const dx = Math.sign(end[0]-start[0]);
    const dy = Math.sign(end[1]-start[1]);
    if (dx == 0) {
        for (let j=start[1]; dy === 1 ? j < end[1] + 1 : j > end[1] - 1; j+=dy) {
            cave[j][start[0]] = '#'
        }
    } else {
        for (let i=start[0]; dx === 1 ? i < end[0] + 1 : i > end[0] - 1; i+=dx) {
            cave[start[1]][i] = '#'
        }
    }
}

rockLines.forEach(l => {
    for (let i=1;i<l.length;i++) {
        drawLine(l[i-1], l[i]);
    }
});

// sand from [500,0] -> 
cave[0][500-offsetX] = '+'

const fall_sand = (x, y) => {
    let moved = true;
    while (moved) {
        if (y === maxY + 1) break
        moved = false
        if (cave[y+1][x] === '.') {
            y++
            moved = true
        } else if (cave[y+1][x-1] === '.') {
            y++, x--
            moved = true
        } else if (cave[y+1][x+1] === '.') {
            y++, x++
            moved = true
        }
    }
    cave[y][x] = 'O'
    return y
}

while (true) {
    const last = fall_sand(500-offsetX, 1)
    if (last === maxY + 1) {
        break;
    }
    sum++;
}

// return cave
cave.map(l => l.join('')).join('\n')
sum
