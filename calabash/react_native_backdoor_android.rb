module ReactNativeBackdoor
    class BackdoorError < RuntimeError
    end

    class << self
        include Calabash::Android::Operations

        BACKDOOR_ENTRYPOINT = ['DecorView index:0', :getContext, :getApplicationContext, :getBackdoor].freeze
        BACKDOOR_EXEC_TIMEOUT = 10

        private def callBackdoor(*args)
            result = query(*BACKDOOR_ENTRYPOINT, *args)

            if result.is_a?(Array) && result.first.is_a?(Hash) && result.first['error']
                Kernel.raise(BackdoorError, "Query #{args} threw #{result.inspect}")
            end

            result.first
        end

        def invoke(name, *args)
            id = callBackdoor(method_name: :invokeMethod, arguments: [name, args]);

            result = nil
            wait_for(timeout: BACKDOOR_EXEC_TIMEOUT, timeout_message: "Backdoor #{name} timed out") do
                result = callBackdoor(:getResult => id)
                result['fulfilled']
            end

            result['value']
        end
    end
end
3
