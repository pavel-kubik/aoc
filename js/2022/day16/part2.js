const data = '%data%'.split('\n').map(line => { 
    line = line.replace('tunnel leads to valve ', 'tunnels lead to valves ');
    return {
        valve: line.substring(6, 8),
        rate: Number(line.split('=')[1].split(';')[0]),
        valves: line.split('valves ')[1].split(', '),
        visited: 0
    }
})

let mapValves = new Map()
data.forEach(v => mapValves.set(v.valve, v))
const notNullValves = [...mapValves.values()].filter(v => v.rate > 0)

let max=0//1965;
let i=0
const processValves = (path, node, time, preasure, maxTime, opened) => {
    i++;
    const remainngValves = notNullValves.filter(v => !opened.has(v.valve)).sort((a,b)=>b.rate-a.rate);
    let timeout = maxTime - time - 1;
    let possibleGain = 0;
    for (let i=0;i<remainngValves.length/2;i++) {
        possibleGain += remainngValves[2*i].rate*(Math.max(0,timeout)) 
            + remainngValves[2*i+1] ? remainngValves[2*i+1].rate*(Math.max(0,timeout)) : 0;
        timeout -= 2;
    }
    if (preasure + possibleGain < max) {
        return preasure
    }
    if (time >= maxTime || opened.size === notNullValves.length) {
        if (preasure > max) {
            max = preasure;
            console.log('New max preasure ' + max + " for path\n" + path.join('\n'))
        }
        return preasure
    }

    const paths = []
    const release0 = node[0].rate*(Math.max(0,maxTime - time - 1));
    const release1 = node[1].rate*(Math.max(0,maxTime - time - 1));
    if (node[0] != node[1] &&
            !opened.has(node[0].valve) && release0 > 0 &&
            !opened.has(node[1].valve) && release1 > 0) {
        // both open valves
        opened.add(node[0].valve);
        opened.add(node[1].valve);
        paths.push(processValves([path[0]+','+node[0].valve+'+', path[1]+','+node[1].valve+'+'], node, time + 1, preasure + release0 + release1, maxTime, opened));
        opened.delete(node[0].valve);
        opened.delete(node[1].valve);
    }
    if (!opened.has(node[0].valve) && release0 > 0) {
        // first open valve
        opened.add(node[0].valve);
        node[1].valves.map(v => mapValves.get(v)).sort((a,b) => a.visited-b.visited).forEach(v => {
            paths.push(processValves([path[0]+', '+node[0].valve+'+', path[1]+', '+v.valve+' '], [node[0], v], time + 1, preasure + release0, maxTime, opened))
        });
        opened.delete(node[0].valve);
    }
    if (!opened.has(node[1].valve) && release1 > 0) {
        // second open valve
        opened.add(node[1].valve);
        node[0].valves.map(v => mapValves.get(v)).sort((a,b) => a.visited-b.visited).forEach(v => {
            paths.push(processValves([path[0]+', '+v.valve+' ', path[1]+', '+node[1].valve+'+'], [v, node[1]], time + 1, preasure + release1, maxTime, opened))
        });
        opened.delete(node[1].valve);
    }
    // both move
    node[0].valves.map(v => mapValves.get(v)).sort((a,b) => a.visited-b.visited).forEach(v0 => {
        node[1].valves.map(v => mapValves.get(v)).sort((a,b) => a.visited-b.visited).forEach(v1 => {
            paths.push(processValves([path[0]+', '+v0.valve,path[1]+', '+v1.valve], [v0, v1], time + 1, preasure, maxTime, opened));
        });
    });
    return Math.max(...paths);
}

let out = processValves(['AA', 'AA'], [mapValves.get('AA'), mapValves.get('AA')], 0, 0, 26, new Set())
out + ' @Iterations ' + i // 1651 @36314 | 1915 @Iterations 422186
