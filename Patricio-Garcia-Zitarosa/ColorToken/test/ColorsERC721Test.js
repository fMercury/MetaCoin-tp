const tokenContract = artifacts.require('./ColorToken.sol');
const BigNumber = web3.BigNumber;

contract('ColorToken', accounts => {
    var owner = accounts[0];
    let colors;

    beforeEach(async function() {
        colors = await tokenContract.new({ from: owner });
    });

    it('initial state', async function() {
        const name = await colors.name();
        const symbol = await colors.symbol();

        assert.equal(name, 'COLORS');
        assert.equal(symbol, 'HEX');
    });

    it('has correct owner', async function() {
        const actualOwner = await colors.owner();
        assert.equal(actualOwner, owner);
    });

    describe('can mint colors', () => {
        let colorId = 16711680;
        let tx;
        let price = new web3.BigNumber(10000000000000000);

        beforeEach(async function() {
            tx = await colors.mint(colorId, { from: owner, value: price });
        });

        it('can mint us a color', async function() {
            assert(colors.ownerOf(colorId), owner);
            assert.equal(tx.logs[0].event, 'Transfer');
        });

        it('can retrieve tokens for the owner', async function() {
            let tokens = await colors.tokensOf(owner);

            assert.equal(tokens.length, 1);
            assert.equal(tokens[(0, new web3.BigNumber(colorId))]);
        });

        it('cant mint tokens that have been minted before', async function() {
            await expectThrow(colors.mint(colorId, { from: owner }));
        });
    });
});
