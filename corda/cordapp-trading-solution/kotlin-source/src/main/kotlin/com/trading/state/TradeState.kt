package com.trading.state

import com.trading.schema.TradeSchemaV1
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState

/**
 * The state object recording Trade agreements between two parties.
 * A state must implement [ContractState] or one of its descendants.
 *
 * @param sellValue sell value of the Trade.
 * @param sellCurrency sell currency for the Trade.
 * @param localContract
 * @param initiatingParty the party initiating the Trade.
 * @param counterParty the Trade counter party.
 * @param tradeStatus the Trade Status.
 * @param linearId Unique ID for the Trade.
 */
data class TradeState(val sellValue: Int,
                      val sellCurrency: String,
                      val longContract: Int,
		      val initiatingParty: Party,
                      val counterParty: Party,
                      val tradeStatus: String,
                      override val linearId: UniqueIdentifier = UniqueIdentifier()):

        LinearState, QueryableState {
    /** The public keys of the involved parties. */
    override val participants: List<AbstractParty> get() = listOf(initiatingParty, counterParty)

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
        return when (schema) {
            is TradeSchemaV1 -> TradeSchemaV1.PersistentTrade(
                    this.initiatingParty.name.toString(),
                    this.counterParty.name.toString(),
                    this.sellValue,
                    this.sellCurrency.toString(),
		    this.longContract,
                    this.tradeStatus,
                    this.linearId.id
            )
            else -> throw IllegalArgumentException("Unrecognised schema $schema")
        }
    }
    override fun supportedSchemas(): Iterable<MappedSchema> = listOf(TradeSchemaV1)
}
