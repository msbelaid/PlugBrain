package app.plugbrain.android.di

import app.plugbrain.android.challenges.Challenge
import app.plugbrain.android.challenges.square.SquareChallengeEasyTwoThroughFive
import app.plugbrain.android.challenges.square.SquareChallengeMediumSixThroughTen
import app.plugbrain.android.challenges.square.SquareChallengeHardElevenThroughTwenty
import app.plugbrain.android.challenges.addition.AdditionFiveDigitsChallenge
import app.plugbrain.android.challenges.addition.AdditionFourDigitsChallenge
import app.plugbrain.android.challenges.addition.AdditionOneAndTwoDigitsChallenge
import app.plugbrain.android.challenges.addition.AdditionPlusZeroOrOneChallenge
import app.plugbrain.android.challenges.addition.AdditionThreeDigitsChallenge
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsCarryFreeChallenge
import app.plugbrain.android.challenges.addition.AdditionTwoDigitsWithCarryChallenge
import app.plugbrain.android.challenges.addition.AdditionUnderFiveChallenge
import app.plugbrain.android.challenges.addition.AdditionUnderTenChallenge
import app.plugbrain.android.challenges.factory.ChallengeFactory
import app.plugbrain.android.challenges.multiplication.MultiplicationByElevenChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationByFiveChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationByTenChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationByTwoChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationByZeroOrOneChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationDoubleByDoubleDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationFourByFourDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationSingleByDoubleDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationSingleByThreeDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationSingleByThreeMultipleTenDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationSingleDigitByMultipleTenChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationSingleDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationThreeByFourDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationThreeByThreeDigitChallenge
import app.plugbrain.android.challenges.multiplication.MultiplicationTwoByThreeDigitChallenge
import app.plugbrain.android.challenges.substraction.SubtractFourDigitsChallenge
import app.plugbrain.android.challenges.substraction.SubtractFromSelfChallenge
import app.plugbrain.android.challenges.substraction.SubtractFromZeroChallenge
import app.plugbrain.android.challenges.substraction.SubtractOneDigitChallenge
import app.plugbrain.android.challenges.substraction.SubtractThreeDigitsChallenge
import app.plugbrain.android.challenges.substraction.SubtractThreeFromTwoDigitsChallenge
import app.plugbrain.android.challenges.substraction.SubtractTwoDigitsChallenge
import app.plugbrain.android.challenges.substraction.SubtractTwoDigitsNoBorrowChallenge
import app.plugbrain.android.challenges.substraction.SubtractTwoFromOneDigitChallenge
import app.plugbrain.android.challenges.substraction.SubtractUnderFiveChallenge
import org.koin.dsl.bind
import org.koin.dsl.module

val challengesModule = module {
  factory { AdditionPlusZeroOrOneChallenge() } bind Challenge::class
  factory { AdditionUnderFiveChallenge() } bind Challenge::class
  factory { AdditionUnderTenChallenge() } bind Challenge::class
  factory { AdditionOneAndTwoDigitsChallenge() } bind Challenge::class
  factory { AdditionTwoDigitsCarryFreeChallenge() } bind Challenge::class
  factory { AdditionTwoDigitsWithCarryChallenge() } bind Challenge::class
  factory { AdditionThreeDigitsChallenge() } bind Challenge::class
  factory { AdditionFourDigitsChallenge() } bind Challenge::class
  factory { AdditionFiveDigitsChallenge() } bind Challenge::class

  factory { SubtractFromZeroChallenge() } bind Challenge::class
  factory { SubtractFromSelfChallenge() } bind Challenge::class
  factory { SubtractUnderFiveChallenge() } bind Challenge::class
  factory { SubtractOneDigitChallenge() } bind Challenge::class
  factory { SubtractTwoFromOneDigitChallenge() } bind Challenge::class
  factory { SubtractTwoDigitsNoBorrowChallenge() } bind Challenge::class
  factory { SubtractTwoDigitsChallenge() } bind Challenge::class
  factory { SubtractThreeFromTwoDigitsChallenge() } bind Challenge::class
  factory { SubtractThreeDigitsChallenge() } bind Challenge::class
  factory { SubtractFourDigitsChallenge() } bind Challenge::class

  factory { MultiplicationByZeroOrOneChallenge() } bind Challenge::class
  factory { MultiplicationByTenChallenge() } bind Challenge::class
  factory { MultiplicationByTwoChallenge() } bind Challenge::class
  factory { MultiplicationByFiveChallenge() } bind Challenge::class
  factory { MultiplicationByElevenChallenge() } bind Challenge::class
  factory { MultiplicationSingleDigitChallenge() } bind Challenge::class
  factory { MultiplicationSingleDigitByMultipleTenChallenge() } bind Challenge::class
  factory { MultiplicationSingleByDoubleDigitChallenge() } bind Challenge::class
  factory { MultiplicationSingleByThreeMultipleTenDigitChallenge() } bind Challenge::class
  factory { MultiplicationSingleByThreeDigitChallenge() } bind Challenge::class
  factory { MultiplicationDoubleByDoubleDigitChallenge() } bind Challenge::class
  factory { MultiplicationTwoByThreeDigitChallenge() } bind Challenge::class
  factory { MultiplicationThreeByThreeDigitChallenge() } bind Challenge::class
  factory { MultiplicationThreeByFourDigitChallenge() } bind Challenge::class
  factory { MultiplicationFourByFourDigitChallenge() } bind Challenge::class
  // New generated challenges will be added here, do not remove

  factory {
    ChallengeFactory(
      getAll<Challenge>().map { instance ->
        { get(instance::class) }
      },
    )
  }
}
