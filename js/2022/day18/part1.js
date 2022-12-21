const points = '%data%'.split('\n').map(l=>l.split(',').map(v=> new Number(v)))

let dim = [0, 0, 0]
points.forEach(v=>dim = dim.map((max,d)=> v[d]>max ? v[d]: max))

const offset = 2;
dim = dim.map(v => v+offset*2+1);
let grid = Array.from(Array(dim[0]), () =>
    Array.from(Array(dim[1]), () =>
        Array.from(Array(dim[2]), () => 0)))

points.forEach(p => {
    grid[p[0]+offset][p[1]+offset][p[2]+offset] = 1;
})

const sides = [
    [-1, 0, 0],
    [1, 0, 0],
    [0, -1, 0],
    [0, 1, 0],
    [0, 0, -1],
    [0, 0, 1],
]
const countFreeEdges = (grid, x, y, z) => {
    let freeEdges = 0
    sides.forEach(s => {
        if (grid[x+s[0]][y+s[1]][z+s[2]] === 0) {
            freeEdges++
        }
    })
    return freeEdges
}

let sum = 0
for(let i=0;i<dim[0];i++) {
    for(let j=0;j<dim[1];j++) {
        for(let k=0;k<dim[2];k++) {
            if (grid[i][j][k] === 1) {
                sum += countFreeEdges(grid, i, j, k);
            }
        }
    }
}

sum