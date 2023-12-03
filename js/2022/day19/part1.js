const input = '%data%'.split('\n').map(line => {
    const items = line.split(':')[1].split('.');
    const ore = items[0].match(/\d+/g).map(Number);
    const clay = items[1].match(/\d+/g).map(Number);
    const obsidian = items[2].match(/\d+/g).map(Number);
    const geode = items[3].match(/\d+/g).map(Number);
    return [
        [ore[0], 0, 0, 0],
        [clay[0], 0, 0, 0],
        [obsidian[0], obsidian[1], 0, 0],
        [geode[0], 0, geode[1], 0]
    ];
});

const START=[1,0,0,0]

const canBuildBot = (botIdx, sources, bp) => {
    const botCosts = bp[botIdx];
    return botCosts.every((cost,i) => cost <= sources[i])
}

const buildBot = (botIdx, sources, bp) => {
    const botCosts = bp[botIdx];
    return sources.map((c,i) => c -= botCosts[i])
}

const add = (a, b) => a.map((c,i) => c + b[i])
const remove = (a, b) => a.map((c,i) => c - b[i])
const mul = (a, b) => a.map((c,i) => c * b[i])

const inc = (a, idx) => a.map((v, i) => i === idx ? v + 1 : v)
const dec = (a, idx) => a.map((v, i) => i === idx ? v - 1 : v)

const estimateBest = (sources, bots, left, bp) => {
    return sources[3] + (bots[3]+left-1)*left;
}

let i = 0;
let max = 0;

let hashes = {}
let botHashes = {}

const evaluateStep = (bots, sources, bp, build, left) => {
    if (i > 10000000) {
        return max;
    }
    // stop condition
    if (left <= 0) {
        return sources[3];
    }
    // hash position speed up
    const hash = 'T'+left+'B'+bots.join(':')+'S'+sources.join(':');
    if (hashes[hash]) {
        return hashes[hash];
    }
    // hash bot speed up
    const botHash = 'T'+left+'B'+bots.join(':');
    if (botHashes[botHash]) {
        const hs = botHashes[botHash];
        const lowerSourcesForSameBotsAndTime = sources.every((s,i) => s <= hs[i]);
        if (lowerSourcesForSameBotsAndTime) {
            return sources[3];
        }
        if (hs[3] < sources[3]) {
            botHashes[botHash] = sources.slice();    
        }
    } else {
        botHashes[botHash] = sources.slice();
    }
    // cut condition
    const estimate = estimateBest(sources, bots, left, bp);
    if (left < 12 && estimate < max) {
        return estimate;
    }
    // collect resources
    sources = add(sources, bots);
    // build bots
    bots = add(bots, build);
    let opt = [];
    //console.log("T " + left + " " + bots + " " + sources + " MAX " + max);
    if (++i % 1000000 === 0) {
        console.log("Step " + i);
    }
    [3,2,1,0].forEach(i => {
        if (canBuildBot(i, sources, bp)) {
            const botPrice = bp[i];
            sources = remove(sources, botPrice)
            //bots = inc(bots, i)
            opt.push(evaluateStep(bots, sources, bp, inc([0,0,0,0], i), left - 1));
            //bots = dec(bots, i)
            sources = add(sources, botPrice)
        }
    });
    opt.push(evaluateStep(bots, sources, bp, [0,0,0,0], left - 1));
    const currentMax = Math.max(...opt);
    if (currentMax > max) {
        max = currentMax;
        console.log("T " + left + " " + bots + " " + sources + " MAX " + max + ' @' + i);
    }
    // hash solved positions
    hashes[hash] = currentMax;
    
    sources = remove(sources, bots);
    bots = remove(bots, build);
    return currentMax;
}

const evaluateBP = (bp, time) => {
    return evaluateStep(START, [0,0,0,0], bp, [0,0,0,0], time);
}

// let output = 0
// input.forEach((bluprint, i) => {
//     hashes = {} // clean hashes for each blueprint
//     botHashes = {}
//     it = 0;
//     max = 0;
//     //if (i === 1) return;
//     const best = evaluateBP(bluprint, 24);
//     output += (i+1)*best;
//     console.log('Blueprint ' + (i+1) + ' found with ' + best);
// })

let output = evaluateBP(input[1], 24)
console.log('Evaluated after stapes: ' + i);
/*
without cut
- T 1 1,6,2,2 2,47,8,9 MAX 9   @3806443   [+endless search of rest]

with cut `sources[3] + (bots[3]+left)*left`
- T 1 1,6,2,2 2,47,8,9 MAX 9    @302079   20000000+ DNF

with cut `sources[3] + (bots[3]+left-1)*left`
- T 1 1,6,2,2 2,47,8,9 MAX 9     @97446   20000000+ DNF

with cut 
- T 1 1,6,2,2 2,47,8,9 MAX 9     @35507     1201340 steps
- 2nd                MAX 12    @5549615     7372925 steps

with cut hash positions and hash bots
- T 1 1,6,2,2 2,47,8,9 MAX 9     @35469     1198435 steps
- 2nd                MAX 12    @5549615     7372925 steps


*/
