/*
 * ========================LICENSE_START=================================
 * AEM Permission Management
 * %%
 * Copyright (C) 2013 Cognifide Limited
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

allprojects {
    repositories {
        mavenLocal()
        jcenter()
        maven ("https://repo.adobe.com/nexus/content/groups/public")
    }

    afterEvaluate {
        val apmRepositoryUsername: String? by extra
        val apmRepositoryPassword: String? by extra
        extensions.findByType(PublishingExtension::class)?.apply {
            publications?.findByName("apm")?.apply {
                if (this is MavenPublication) {
                    pom {
                        name.set("AEM Permission Management")
                        description.set("AEM Permission Management is an AEM based tool focused on streamlining the permission configuration")
                        url.set("https://github.com/Cognifide/APM")
                        licenses {
                            license {
                                name.set("The Apache License, Version 2.0")
                                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            }
                        }
                        developers {
                            developer {
                                name.set("Marcin Jędraszczyk")
                                email.set("marcin.jedraszczyk@cognifide.com")
                                organization.set("Cognifide")
                                organizationUrl.set("https://www.cognifide.com")
                            }
                            developer {
                                name.set("Katarzyna Wielgosz")
                                email.set("katarzyna.wielgosz@cognifide.com")
                                organization.set("Cognifide")
                                organizationUrl.set("https://www.cognifide.com")
                            }
                            developer {
                                name.set("Piotr Wilczyński")
                                email.set("piotr.wilczynski@cognifide.com")
                                organization.set("Cognifide")
                                organizationUrl.set("https://www.cognifide.com")
                            }
                            developer {
                                name.set("Paweł Przystarz")
                                email.set("pawel.przystarz@cognifide.com")
                                organization.set("Cognifide")
                                organizationUrl.set("https://www.cognifide.com")
                            }
                            developer {
                                name.set("Urszula Gawłowska")
                                email.set("urszula.gawlowska@cognifide.com")
                                organization.set("Cognifide")
                                organizationUrl.set("https://www.cognifide.com")
                            }
                            developer {
                                name.set("Marek Krokosiński")
                                email.set("marek.krokosinski@cognifide.com")
                                organization.set("Cognifide")
                                organizationUrl.set("https://www.cognifide.com")
                            }
                            developer {
                                name.set("Szymon Owczarz")
                                email.set("szymon.owczarz@cognifide.com")
                                organization.set("Cognifide")
                                organizationUrl.set("https://www.cognifide.com")
                            }
                            developer {
                                name.set("Bartosz Wesołowski")
                                email.set("bartosz.wesolowski@cognifide.com")
                                organization.set("Cognifide")
                                organizationUrl.set("https://www.cognifide.com")
                            }
                            developer {
                                name.set("Artur Maćkowiak")
                                email.set("artur.mackowiak@cognifide.com")
                                organization.set("Cognifide")
                                organizationUrl.set("https://www.cognifide.com")
                            }
                            developer {
                                name.set("Tomasz Kaik")
                                email.set("tomasz.kaik@cognifide.com")
                                organization.set("Cognifide")
                                organizationUrl.set("https://www.cognifide.com")
                            }
                            developer {
                                name.set("Dominik Przybył")
                                email.set("dominik.przybyl@cognifide.com")
                                organization.set("Cognifide")
                                organizationUrl.set("https://www.cognifide.com")
                            }
                        }
                        scm {
                            connection.set("https://github.com/Cognifide/APM.git")
                            developerConnection.set("https://github.com/Cognifide/APM.git")
                            url.set("https://github.com/Cognifide/APM")
                        }
                    }
                }
            }
            repositories {
                maven {
                    name = "OSSSonatypeOrg"
                    url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = apmRepositoryUsername
                        password = apmRepositoryPassword
                    }
                    authentication {
                        create<BasicAuthentication>("basic")
                    }
                }
            }
        }

        extensions.findByType(SigningExtension::class)?.apply {
            useGpgCmd()
            val publication = extensions.findByType(PublishingExtension::class)?.publications?.findByName("apm")
            if (publication != null) {
                sign(publication)
            }
        }
    }
}