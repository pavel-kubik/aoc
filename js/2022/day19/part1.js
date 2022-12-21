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
    // build all robots i can do now
    // collect all resources
    // build robots for collected resorces
    // collect with new robots
    let bestSources = sources.slice();
    add(sources, mul(bots, [left, left, left]));   // collect with all existings bots
    bp[3]
    return bots[3]*left + sources[3];
}

let i = 0;
let max = 0;

const evaluateStep = (bots, sources, bp, build, left) => {
    // stop condition
    if (left <= 0) {
        return sources[3];
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
    [...Array(4).keys()].reverse().forEach(i => {   // obsidian first
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
    sources = remove(sources, bots);
    bots = remove(bots, build);
    return currentMax;
}

const evaluateBP = (bp, time) => {
    return evaluateStep(START, [0,0,0,0], bp, [0,0,0,0], time);
}

/*
without cut
- T 1 1,6,2,2 2,47,8,9 MAX 9 @3806443   [+endless search of rest]
*/
evaluateBP(input[0], 24)
//input.join('\n')