package com.ocadotechnology.doubloons

import sttp.model.Uri
import pureconfig._
import pureconfig.generic.derivation.default._

final case class AppConfig(
    oidc: OIDCConfig // open id connect
) derives ConfigReader

case class OIDCConfig(
    baseUrl: String,
    redirectUri: String,
    clientId: String,
    clientSecret: String,
    scope: String
) derives ConfigReader
