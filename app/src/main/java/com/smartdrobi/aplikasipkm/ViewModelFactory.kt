package com.smartdrobi.aplikasipkm

/*
class ViewModelFactory private constructor(
    private val repository: Repository
): ViewModelProvider.NewInstanceFactory() {

    private var args:Bundle? = null

    fun setArguments(args: Bundle?){
        this.args = args
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        modelClass.let out@ {
            when {
                it.isAssignableFrom(AddBridgeCheckFormViewModel::class.java) -> {
                    val currArgs = args ?: return@out

                    return AddBridgeCheckFormViewModel(repository, currArgs) as T
                }
                it.isAssignableFrom(AddBridgeFormViewModel::class.java) -> {
                    return AddBridgeFormViewModel(repository) as T
                }
                it.isAssignableFrom(DetailViewModel::class.java) -> {
                    return DetailViewModel(repository) as T
                }
                else -> return@out
            }
        }
        args = null
        return super.create(modelClass)
    }

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory?=null

        fun getInstance(
            applicationContext: Context,
            args:Bundle? = null
        ): ViewModelFactory {
            return INSTANCE?.also {
                it.setArguments(args)
            } ?: synchronized(ViewModelFactory::class.java){
                INSTANCE ?: ViewModelFactory(
                    Injection.provideRepository(
                        applicationContext
                    )
                ).also {
                    it.setArguments(args)
                }
            }.also {
                INSTANCE = it
            }
        }
    }
}*/
