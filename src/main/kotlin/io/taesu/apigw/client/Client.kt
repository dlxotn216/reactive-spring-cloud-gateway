package io.taesu.apigw.client

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
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
@Table("APP_CLIENT")
data class Client(
    @Id
    @Column("CLIENT_KEY")
    val key: Long = 0L,

    @Column("CLIENT_ID")
    val id: String,

    @Column("CLIENT_SECRET")
    val secret: String,

    @Column("CONNECT_URL")
    val connectURL: String,
) : Persistable<Long> {

    // Persistable 인터페이스 구현하여 새로운 엔티티인지 구별해 줄 수 있음
    // key를 nullable 하게 주지 않아도 좋음.
    override fun getId(): Long = key

    override fun isNew(): Boolean = key == 0L

}

interface ClientRepository : ReactiveCrudRepository<Client, Long> {
    fun findById(id: String): Mono<Client>
}