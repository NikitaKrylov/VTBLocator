package com.example.misisvtbhack.data

data class Services(
    val blind: Blind,
    val nfcForBankCards: NfcForBankCards,
    val qrRead: QrRead,
    val supportsChargeRub: SupportsChargeRub,
    val supportsEur: SupportsEur,
    val supportsRub: SupportsRub,
    val supportsUsd: SupportsUsd,
    val wheelchair: Wheelchair
)