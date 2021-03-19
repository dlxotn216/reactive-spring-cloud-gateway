package io.taesu.apigw.client

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

/**
 * Created by itaesu on 2021/03/19.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Table("app_client")
class Client(
    @Id
    @Column("client_key")
    val key: Long,

    @Column("client_id")
    val id: String,

    @Column("client_secret")
    val secret: String,

    @Column("connect_url")
    val connectURL: String,
)

interface ClientRepository : ReactiveCrudRepository<Client, Long> {
    fun findById(id: String): Mono<Client>
}