package codeasus.projects.data.util

interface Mapper<Entity, Model> {
    fun mapToModel(entity: Entity): Model
    fun mapToEntity(model: Model): Entity
}