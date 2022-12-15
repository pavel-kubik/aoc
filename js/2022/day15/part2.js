const LINE = 2000000

const data = '%data%'.split('\n').map(line => {
    return line.split(': ').map(text => {
        const coordinates = text.split(' at ')[1].split(', ');
        return {
            x: Number(coordinates[0].substring(2)),
            y: Number(coordinates[1].substring(2))
        };
    });
})
console.log(data);

const dist = (a, b) => {
    return Math.abs(a.x - b.x) + Math.abs(a.y - b.y)
}

const countLine = (line) => {
    let intervals = []
    data.forEach(l => {
        const distance = dist(l[0], l[1]);
        //console.log('Sensor ' + JSON.stringify(l[0]) + ' and beacon ' + JSON.stringify(l[1]) + ' distance ' + distance);
        if (l[0].y - distance < line && line < l[0].y + distance) {
            //console.log('  -> intersect line ' + line)
            const hDist = distance - Math.abs(l[0].y - line);
            intervals.push({
                from: l[0].x - hDist,
                to: l[0].x + hDist
            });
            //console.log('From ' + JSON.stringify(l[0].x - hDist) + ' to ' + JSON.stringify(l[0].x + hDist));
        }
    })

    return intervals;
}

const freeSpots = (line, from, to) => {
    if (line % 100000 === 0) {
        //console.log('Searching distress signal on line ' + line + " from " + from + " to " + to)
    }
    let intervals = countLine(line);
    intervals = intervals.sort((a, b) => Math.sign(a.from - b.from))
    // merge intervals
    const mergeIntervals = (a,b) => {
        if (a.to >= b.from-1) {
            return [{
                from: a.from,
                to: Math.max(a.to, b.to)
            }]
        } else {
            return [a, b]
        }
    }
    let nextIntervals = [];
    let prevInterval = intervals[0]
    for (let i=1;i<intervals.length;i++) {
        const mergedIntervals = mergeIntervals(prevInterval, intervals[i]);
        if (mergedIntervals.length === 2) {
            nextIntervals.push(mergedIntervals[0]);
            prevInterval = mergedIntervals[1];
        } else {
            prevInterval = mergedIntervals[0];
        }
    }
    nextIntervals.push(prevInterval);
    const freeSpots = new Set()
    let counter = 0;
    for(let i=0;i<nextIntervals.length;i++) {
        if (nextIntervals[i].to < 0) continue
        if (counter < nextIntervals[i].from) {
            console.log('Distress signal ' + line + ':' + counter);
            console.log('OUT: ' + (4000000*counter + line));
            freeSpots.add([line, counter]);
            break;
        }
        counter = Math.max(nextIntervals[i].to, 0) + 1
        if (counter > to) break;
    }
    //console.log('Line ' + line + ' ' + freeSpots)
}

//const SIZE = 20;
const SIZE = 4000000;
for(let i=11;i<=SIZE;i++) {
    freeSpots(i, 0, SIZE)
}
